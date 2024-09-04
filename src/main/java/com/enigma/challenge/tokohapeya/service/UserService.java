package com.enigma.challenge.tokohapeya.service;

import com.enigma.challenge.tokohapeya.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserAccount getByUserId(String id);
    UserAccount getByContext();
    void deleteById(String id);
}
