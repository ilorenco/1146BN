package com.example.auth_service.application.auth;

import com.example.auth_service.application.ports.TokenService;
import com.example.auth_service.interfaces.rest.dto.auth.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RefreshTokenHandler {
    
    private final TokenService tokenService;
    
    public TokenResponse handle(String refreshToken) {
        try {
            TokenService.TokenPair pair = ((com.example.auth_service.infrastructure.security.JwtTokenService) tokenService)
                .refresh(refreshToken);
            return new TokenResponse(pair.token(), pair.refreshToken(), pair.expiresIn());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
    }
}
