package com.enigma.challenge.tokohapeya.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JsonResponse<T> {
    private Integer statusCode;
    private String message;
    private T data;
}
