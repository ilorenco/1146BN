package com.example.auth_service.interfaces.rest.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta contendo os tokens JWT para autenticação")
public record TokenResponse (
    @Schema(description = "Token de acesso JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String accessToken,
    
    @Schema(description = "Token de refresh JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String refreshToken,
    
    @Schema(description = "Tempo de expiração do token em segundos", example = "3600")
    long expiresIn
) {}