package com.enigma.challenge.tokohapeya.controller;

import com.enigma.challenge.tokohapeya.dto.request.LoginRequest;
import com.enigma.challenge.tokohapeya.dto.request.RegisterRequest;
import com.enigma.challenge.tokohapeya.dto.response.JsonResponse;
import com.enigma.challenge.tokohapeya.dto.response.LoginResponse;
import com.enigma.challenge.tokohapeya.dto.response.RegisterResponse;
import com.enigma.challenge.tokohapeya.helper.ApiUrl;
import com.enigma.challenge.tokohapeya.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiUrl.AUTHENTICATION_API)
public class AuthController {
    private final AuthService authService;

    @PostMapping(
            path = ApiUrl.PATH_REGISTER,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JsonResponse<?>> register(@RequestBody RegisterRequest request) {
        RegisterResponse register = authService.register(request);
        JsonResponse<RegisterResponse> response = JsonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(register)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(
            path = ApiUrl.PATH_LOGIN,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JsonResponse<?>> login(@RequestBody LoginRequest request) {
        LoginResponse login = authService.login(request);
        JsonResponse<LoginResponse> response = JsonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase() + ". Login success")
                .data(login)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
