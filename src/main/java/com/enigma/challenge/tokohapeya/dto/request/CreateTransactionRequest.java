package com.enigma.challenge.tokohapeya.dto.request;

import com.enigma.challenge.tokohapeya.entity.TransactionDetail;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTransactionRequest {
    @NotBlank(message = "Id user required")
    private String userId;

    private List<CreateTransactionDetailRequest> transactionDetails;
}
