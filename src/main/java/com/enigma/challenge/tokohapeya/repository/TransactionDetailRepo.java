package com.enigma.challenge.tokohapeya.repository;

import com.enigma.challenge.tokohapeya.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailRepo extends JpaRepository<TransactionDetail, String> {
}
