package com.example.auth_service.domain.refresh_token.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class TokenHash {
    
    @Column(name = "token_hash", nullable = false, length = 255)
    private String value;
    
    public TokenHash(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Token hash cannot be null or empty");
        }
        this.value = value;
    }
    
    public static TokenHash of(String value) {
        return new TokenHash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
