package com.example.auth_service.interfaces.rest.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de um novo usuário")
public record UserRequest (
    @Schema(description = "Nome completo do usuário", example = "João Silva", required = true)
    @NotBlank String name,
    
    @Schema(description = "Email do usuário", example = "joao@example.com", required = true)
    @NotBlank @Email String email,
    
    @Schema(description = "Senha do usuário (mínimo 8 caracteres)", example = "minhasenha123", required = true, minLength = 8)
    @NotBlank @Size(min = 8) String password
){
}