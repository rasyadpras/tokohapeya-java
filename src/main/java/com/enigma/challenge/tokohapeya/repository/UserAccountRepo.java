package com.enigma.challenge.tokohapeya.repository;

import com.enigma.challenge.tokohapeya.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount, String> {
    Optional<UserAccount> findByUsername(String username);
}
