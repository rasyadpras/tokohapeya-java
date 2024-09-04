package com.enigma.challenge.tokohapeya.service;

import com.enigma.challenge.tokohapeya.dto.request.LoginRequest;
import com.enigma.challenge.tokohapeya.dto.request.RegisterRequest;
import com.enigma.challenge.tokohapeya.dto.response.LoginResponse;
import com.enigma.challenge.tokohapeya.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
