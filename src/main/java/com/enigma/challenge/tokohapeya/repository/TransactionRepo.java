package com.enigma.challenge.tokohapeya.repository;

import com.enigma.challenge.tokohapeya.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, String> {
}
