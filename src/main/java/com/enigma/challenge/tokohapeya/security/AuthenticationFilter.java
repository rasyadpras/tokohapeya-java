package com.enigma.challenge.tokohapeya.security;

import com.enigma.challenge.tokohapeya.dto.response.JwtClaims;
import com.enigma.challenge.tokohapeya.entity.UserAccount;
import com.enigma.challenge.tokohapeya.service.JwtService;
import com.enigma.challenge.tokohapeya.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtService jwtService;
    final String AUTH_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String bearerToken = request.getHeader(AUTH_HEADER);
            if (bearerToken != null && jwtService.verifyToken(bearerToken)) {
                JwtClaims decodeJwt = jwtService.getJwtClaims(bearerToken);
                UserAccount accountBySub = userService.getByUserId(decodeJwt.getAccountId());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        accountBySub.getUsername(),
                        null,
                        accountBySub.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            log.error("Error in authentication filter : ", e.getMessage());
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
