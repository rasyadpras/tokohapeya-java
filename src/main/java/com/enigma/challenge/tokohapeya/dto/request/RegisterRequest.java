package com.enigma.challenge.tokohapeya.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "E-mail required")
    private String email;

    @NotBlank(message = "Username required")
    private String username;

    @NotBlank(message = "Password required")
    private String password;

    @NotBlank(message = "Name must be not blank")
    private String name;

    private String address;

    @NotBlank(message = "Birth date must be not blank")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid format date (format : yyyy-MM-dd)")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String birthDate;
}
