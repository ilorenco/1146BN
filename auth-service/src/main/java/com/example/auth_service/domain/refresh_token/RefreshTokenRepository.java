package com.example.auth_service.domain.refresh_token;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    
    RefreshToken save(RefreshToken refreshToken);
    
    Optional<RefreshToken> findActiveByHash(String tokenHash);
    
    void revoke(UUID id);
    
    void deleteById(UUID id);
    
    void revokeAllByUserId(UUID userId);
}
