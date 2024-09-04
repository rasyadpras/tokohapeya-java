package com.enigma.challenge.tokohapeya.dto.request;

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
public class CreateTransactionDetailRequest {
    @NotBlank(message = "Id transaction detail required")
    private String productId;

    @NotNull(message = "Quantity must be not null")
    @Min(value = 0, message = "Value must be greater than or equal to 0")
    private Integer quantity;
}
