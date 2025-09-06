package com.example.auth_service.infrastructure.persistence;

import com.example.auth_service.domain.refresh_token.RefreshToken;
import com.example.auth_service.domain.refresh_token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaRefreshTokenRepository implements RefreshTokenRepository {
    
    private final SpringDataRefreshTokenJpa springDataRefreshTokenJpa;
    
    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return springDataRefreshTokenJpa.save(refreshToken);
    }
    
    @Override
    public Optional<RefreshToken> findActiveByHash(String tokenHash) {
        return springDataRefreshTokenJpa.findByTokenHashValueAndRevokedFalseAndExpiresAtValueAfter(
            tokenHash, Instant.now()
        );
    }
    
    @Override
    public void revoke(UUID id) {
        springDataRefreshTokenJpa.revokeById(id);
    }
    
    @Override
    public void deleteById(UUID id) {
        springDataRefreshTokenJpa.deleteById(id);
    }
    
    @Override
    public void revokeAllByUserId(UUID userId) {
        springDataRefreshTokenJpa.revokeAllByUserId(userId);
    }
}
