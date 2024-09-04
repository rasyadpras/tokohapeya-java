package com.enigma.challenge.tokohapeya.service;

import com.enigma.challenge.tokohapeya.dto.request.CreateTransactionRequest;
import com.enigma.challenge.tokohapeya.dto.response.TransactionResponse;
import com.enigma.challenge.tokohapeya.entity.Transaction;

import java.util.List;

public interface TransactionService {
    TransactionResponse create(CreateTransactionRequest transaction);
    TransactionResponse getById(String id);
    List<TransactionResponse> getAll();
}
