package com.example.auth_service.interfaces.rest.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para login com email e senha")
public record PasswordLoginRequest(
        @Schema(description = "Email do usuário", example = "usuario@example.com", required = true)
        @NotBlank @Email String email,
        
        @Schema(description = "Senha do usuário", example = "minhasenha123", required = true)
        @NotBlank String password
) {
}