package com.enigma.challenge.tokohapeya.service.implement;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.challenge.tokohapeya.dto.response.JwtClaims;
import com.enigma.challenge.tokohapeya.entity.UserAccount;
import com.enigma.challenge.tokohapeya.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final String JWT_SECRET;
    private final String ISSUER;
    private final long JWT_EXPIRATION;

    public JwtServiceImpl(
            @Value("${tokohapeya.jwt.secret_key}") String JWT_SECRET,
            @Value("${tokohapeya.jwt.issuer}") String ISSUER,
            @Value("${tokohapeya.jwt.exp}") long JWT_EXPIRATION) {
        this.JWT_SECRET = JWT_SECRET;
        this.ISSUER = ISSUER;
        this.JWT_EXPIRATION = JWT_EXPIRATION;
    }

    @Override
    public String generateToken(UserAccount account) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            return JWT.create()
                    .withSubject(account.getId())
                    .withClaim("roles", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(JWT_EXPIRATION))
                    .withIssuer(ISSUER)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JWT generation failed");
        }
    }

    private String parseJwtToken(String token) {
        log.debug("Test token : {}", token);
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    @Override
    public boolean verifyToken(String bearerToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            String token = parseJwtToken(bearerToken);
            verifier.verify(token);
            return true;
        } catch (JWTCreationException e) {
            log.error("JWT verification failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public JwtClaims getJwtClaims(String bearerToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            String token = parseJwtToken(bearerToken);
            DecodedJWT decodedJWT = verifier.verify(token);
            return JwtClaims.builder()
                    .accountId(decodedJWT.getSubject())
                    .roles(decodedJWT.getClaim("roles").asList(String.class))
                    .build();
        } catch (JWTCreationException e) {
            log.error("JWT verification failed: {}", e.getMessage());
            return null;
        }
    }
}
