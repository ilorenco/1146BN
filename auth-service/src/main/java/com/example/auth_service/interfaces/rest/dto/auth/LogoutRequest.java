package com.example.auth_service.interfaces.rest.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requisição para logout do usuário")
public record LogoutRequest(
    @Schema(description = "Token de refresh para revogar", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    @NotBlank(message = "Refresh token é obrigatório")
    String refreshToken
) {}
