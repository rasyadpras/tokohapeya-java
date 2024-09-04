package com.enigma.challenge.tokohapeya.controller;

import com.enigma.challenge.tokohapeya.dto.response.JsonResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.zip.DataFormatException;

@RestControllerAdvice
public class ErrorExceptionHandler {
    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<JsonResponse<?>> responseStatusError(ResponseStatusException e) {
        JsonResponse<?> response = JsonResponse.builder()
                .statusCode(e.getStatusCode().value())
                .message(e.getReason())
                .build();
        return ResponseEntity.status(e.getStatusCode()).body(response);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<JsonResponse<?>> constraintViolationError(ConstraintViolationException e) {
        JsonResponse<?> response = JsonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({DataFormatException.class})
    public ResponseEntity<JsonResponse<?>> dataIntegrityViolationError(DataIntegrityViolationException e) {
        JsonResponse.JsonResponseBuilder<Object> builder = JsonResponse.builder();
        HttpStatus httpStatus;

        if (e.getMessage().contains("foreign key constraint")) {
            builder.statusCode(HttpStatus.BAD_REQUEST.value());
            builder.message("Cannot delete data because other table depend on it");
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (e.getMessage().contains("unique constraint") || e.getMessage().contains("Duplicate entry")) {
            builder.statusCode(HttpStatus.CONFLICT.value());
            builder.message("Data already exists");
            httpStatus = HttpStatus.CONFLICT;
        } else {
            builder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            builder.message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(httpStatus).body(builder.build());
    }
}
