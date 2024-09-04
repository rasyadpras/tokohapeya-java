package com.enigma.challenge.tokohapeya.service.implement;

import com.enigma.challenge.tokohapeya.entity.TransactionDetail;
import com.enigma.challenge.tokohapeya.repository.TransactionDetailRepo;
import com.enigma.challenge.tokohapeya.service.TransactionDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionDetailServiceImpl implements TransactionDetailService {
    private final TransactionDetailRepo transactionDetailRepo;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TransactionDetail> createBulk(List<TransactionDetail> transactionDetails) {
        return transactionDetailRepo.saveAllAndFlush(transactionDetails);
    }
}
