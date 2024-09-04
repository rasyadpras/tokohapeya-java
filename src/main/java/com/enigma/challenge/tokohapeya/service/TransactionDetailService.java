package com.enigma.challenge.tokohapeya.service;

import com.enigma.challenge.tokohapeya.entity.TransactionDetail;

import java.util.List;

public interface TransactionDetailService {
    List<TransactionDetail> createBulk(List<TransactionDetail> transactionDetails);
}
