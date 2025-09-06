package com.example.auth_service.domain.refresh_token;

import com.example.auth_service.domain.refresh_token.vo.ExpiresAt;
import com.example.auth_service.domain.refresh_token.vo.TokenHash;
import com.example.auth_service.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {
    
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Embedded
    private TokenHash tokenHash;
    
    @Embedded
    private ExpiresAt expiresAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    public RefreshToken(TokenHash tokenHash, ExpiresAt expiresAt, User user) {
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.user = user;
        this.createdAt = Instant.now();
        this.revoked = false;
    }
    
    public static RefreshToken create(TokenHash tokenHash, ExpiresAt expiresAt, User user) {
        return new RefreshToken(tokenHash, expiresAt, user);
    }
    
    public boolean isExpired() {
        return expiresAt.isExpired();
    }
    
    public boolean isActive() {
        return !revoked && !isExpired();
    }
    
    public void revoke() {
        this.revoked = true;
    }
}
