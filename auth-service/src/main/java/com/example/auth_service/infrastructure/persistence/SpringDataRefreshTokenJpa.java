package com.example.auth_service.infrastructure.persistence;

import com.example.auth_service.domain.refresh_token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataRefreshTokenJpa extends JpaRepository<RefreshToken, UUID> {
    
    Optional<RefreshToken> findByTokenHashValueAndRevokedFalseAndExpiresAtValueAfter(
        String tokenHash, Instant now
    );
    
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.id = :id")
    void revokeById(@Param("id") UUID id);
    
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user.id = :userId")
    void revokeAllByUserId(@Param("userId") UUID userId);
}
