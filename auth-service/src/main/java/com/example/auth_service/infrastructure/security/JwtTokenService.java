package com.example.auth_service.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.auth_service.application.ports.TokenService;
import com.example.auth_service.domain.refresh_token.RefreshToken;
import com.example.auth_service.domain.refresh_token.RefreshTokenRepository;
import com.example.auth_service.domain.refresh_token.vo.ExpiresAt;
import com.example.auth_service.domain.refresh_token.vo.TokenHash;
import com.example.auth_service.domain.user.User;
import com.example.auth_service.infrastructure.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenService implements TokenService {

    private final JwtProperties props;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public TokenPair issue(User user) {
        if  (props.getSecret() == null || props.getSecret().isEmpty()) {
            throw new IllegalArgumentException("secret is required (jwt.secret)");
        }

        Instant now = Instant.now();
        Algorithm algorithm = Algorithm.HMAC256(props.getSecret().getBytes(StandardCharsets.UTF_8));

        Instant accessExp = now.plusSeconds(props.getAccessTtlSeconds());
        String accessToken = JWT.create()
                .withIssuer(props.getIssuer())
                .withAudience(props.getAudience())
                .withSubject(user.getId().toString())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(accessExp))
                .withClaim("type", "access")
                .withClaim("email", user.getEmail().getValue())
                .withClaim("role", user.getRole().getValue().name())
                .withClaim("level", user.getRole().getValue().getLevel())
                .sign(algorithm);

        String refreshTokenValue = generateRefreshToken();
        String refreshTokenHash = hashRefreshToken(refreshTokenValue);
        
        TokenHash tokenHash = TokenHash.of(refreshTokenHash);
        ExpiresAt expiresAt = ExpiresAt.fromNow(props.getRefreshTtlSeconds());
        RefreshToken refreshToken = RefreshToken.create(tokenHash, expiresAt, user);
        refreshTokenRepository.save(refreshToken);

        return new TokenPair(accessToken, refreshTokenValue, (int) props.getAccessTtlSeconds());
    }
    
    public TokenPair refresh(String refreshTokenValue) {
        String refreshTokenHash = hashRefreshToken(refreshTokenValue);
        
        return refreshTokenRepository.findActiveByHash(refreshTokenHash)
            .map(refreshToken -> {
                User user = refreshToken.getUser();
                
                refreshToken.revoke();
                refreshTokenRepository.save(refreshToken);
                
                return issue(user);
            })
            .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
    }
    
    public void revokeRefreshToken(String refreshTokenValue) {
        String refreshTokenHash = hashRefreshToken(refreshTokenValue);
        
        refreshTokenRepository.findActiveByHash(refreshTokenHash)
            .ifPresent(refreshToken -> {
                refreshToken.revoke();
                refreshTokenRepository.save(refreshToken);
            });
    }
    
    private String generateRefreshToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
    
    private String hashRefreshToken(String refreshToken) {
        return String.valueOf(refreshToken.hashCode());
    }
}
