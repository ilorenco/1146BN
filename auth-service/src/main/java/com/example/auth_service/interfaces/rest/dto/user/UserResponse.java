package com.example.auth_service.interfaces.rest.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Dados de resposta de um usuário")
public record UserResponse(
        @Schema(description = "ID único do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        
        @Schema(description = "Nome completo do usuário", example = "João Silva")
        String name,
        
        @Schema(description = "Email do usuário", example = "joao@example.com")
        String email,
        
        @Schema(description = "Papel/função do usuário no sistema", example = "USER")
        String role
) {
}