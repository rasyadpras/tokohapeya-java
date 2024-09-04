package com.enigma.challenge.tokohapeya.controller;

import com.enigma.challenge.tokohapeya.dto.request.CreateTransactionRequest;
import com.enigma.challenge.tokohapeya.dto.response.JsonResponse;
import com.enigma.challenge.tokohapeya.dto.response.TransactionResponse;
import com.enigma.challenge.tokohapeya.helper.ApiUrl;
import com.enigma.challenge.tokohapeya.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiUrl.TRANSACTION_API)
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JsonResponse<TransactionResponse>> createTransaction(
            @RequestBody CreateTransactionRequest transaction
    ) {
        TransactionResponse create = transactionService.create(transaction);
        JsonResponse<TransactionResponse> response = JsonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(create)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse<List<TransactionResponse>>> getAllTransactions() {
        List<TransactionResponse> transactions = transactionService.getAll();
        JsonResponse<List<TransactionResponse>> response = JsonResponse.<List<TransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(transactions)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = ApiUrl.PATH_ID,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JsonResponse<TransactionResponse>> getTransactionById(@PathVariable String id) {
        TransactionResponse transactionById = transactionService.getById(id);
        JsonResponse<TransactionResponse> response = JsonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(transactionById)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
