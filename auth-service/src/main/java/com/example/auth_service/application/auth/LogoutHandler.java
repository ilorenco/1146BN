package com.example.auth_service.application.auth;

import com.example.auth_service.application.ports.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutHandler {
    
    private final TokenService tokenService;
    
    public void handle(String refreshToken) {
        try {
            ((com.example.auth_service.infrastructure.security.JwtTokenService) tokenService)
                .revokeRefreshToken(refreshToken);
        } catch (Exception e) {
        }
    }
}
