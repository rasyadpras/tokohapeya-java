package com.enigma.challenge.tokohapeya.service;

import com.enigma.challenge.tokohapeya.dto.response.JwtClaims;
import com.enigma.challenge.tokohapeya.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount account);
    boolean verifyToken(String token);
    JwtClaims getJwtClaims(String token);
}
