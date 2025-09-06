package com.example.auth_service.interfaces.rest.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requisição para renovar token de acesso usando refresh token")
public record RefreshTokenRequest(
    @Schema(description = "Token de refresh válido", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    @NotBlank(message = "Refresh token é obrigatório")
    String refreshToken
) {}
