package com.enigma.challenge.tokohapeya.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class UpdateProfileRequest {
    @JsonIgnore
    @NotBlank(message = "Id profile required")
    private String id;

    @NotBlank(message = "Name must be not blank")
    private String name;

    private String address;

    @NotBlank(message = "Date must be not blank")
    @JsonFormat(pattern = "YYYY-MM-DD")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format (YYYY-MM-DD)")
    private String birthDate;
}
