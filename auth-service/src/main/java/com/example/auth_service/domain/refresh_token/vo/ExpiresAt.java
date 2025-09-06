package com.example.auth_service.domain.refresh_token.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class ExpiresAt {
    
    @Column(name = "expires_at", nullable = false)
    private Instant value;
    
    public ExpiresAt(Instant value) {
        if (value == null) {
            throw new IllegalArgumentException("Expires at cannot be null");
        }
        if (value.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Expires at cannot be in the past");
        }
        this.value = value;
    }
    
    public static ExpiresAt of(Instant value) {
        return new ExpiresAt(value);
    }
    
    public static ExpiresAt fromNow(long secondsFromNow) {
        return new ExpiresAt(Instant.now().plusSeconds(secondsFromNow));
    }
    
    public boolean isExpired() {
        return Instant.now().isAfter(value);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}
