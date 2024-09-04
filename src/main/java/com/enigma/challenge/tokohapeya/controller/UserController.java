package com.enigma.challenge.tokohapeya.controller;

import com.enigma.challenge.tokohapeya.dto.request.UpdateProfileRequest;
import com.enigma.challenge.tokohapeya.dto.response.JsonResponse;
import com.enigma.challenge.tokohapeya.dto.response.ProfileResponse;
import com.enigma.challenge.tokohapeya.helper.ApiUrl;
import com.enigma.challenge.tokohapeya.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiUrl.USER_API)
public class UserController {
    private final ProfileService profileService;

    @GetMapping(
            path = ApiUrl.PATH_ID,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JsonResponse<ProfileResponse>> getUserById(@PathVariable String id) {
        ProfileResponse profileById = profileService.getById(id);
        JsonResponse<ProfileResponse> response = JsonResponse.<ProfileResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(profileById)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse<List<ProfileResponse>>> getAllUsers() {
        List<ProfileResponse> profiles = profileService.getAll();
        JsonResponse<List<ProfileResponse>> response = JsonResponse.<List<ProfileResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(profiles)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PutMapping(
            path = ApiUrl.PATH_ID,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JsonResponse<ProfileResponse>> updateUser(
            @PathVariable String id,
            @RequestBody UpdateProfileRequest user
    ) {
        user.setId(id);
        ProfileResponse update = profileService.update(user);
        JsonResponse<ProfileResponse> response = JsonResponse.<ProfileResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(update)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(
            path = ApiUrl.PATH_ID,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JsonResponse<String>> deleteUser(@PathVariable String id) {
        profileService.deleteById(id);
        JsonResponse<String> response = JsonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase() + ". Deleted user with id " + id)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
