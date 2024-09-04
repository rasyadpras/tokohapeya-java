package com.enigma.challenge.tokohapeya.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {
    @JsonIgnore
    @NotBlank(message = "Id product required")
    private String id;

    @NotBlank(message = "Brand name must be not blank")
    private String brand;

    @NotBlank(message = "Type phone must be not blank")
    private String type;

    @NotNull(message = "Price must be not null")
    @Min(value = 0, message = "Value must be greater than or equal to 0")
    private Long price;

    @NotNull(message = "Stock must be not null")
    @Min(value = 0, message = "Value must be greater than or equal to 0")
    private Integer stock;
}
