package com.example.auth_service.interfaces.rest;

import com.example.auth_service.application.user.ListUsersHandler;
import com.example.auth_service.application.user.RegisterUserHandler;
import com.example.auth_service.interfaces.rest.dto.user.UserRequest;
import com.example.auth_service.interfaces.rest.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping( "/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints para gerenciamento de usuários")
public class UserController {

    private final ListUsersHandler list;
    private final RegisterUserHandler register;

    @Operation(
        summary = "Listar usuários", 
        description = "Retorna uma lista paginada de usuários cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200", 
                description = "Lista de usuários retornada com sucesso",
                content = @Content(
                    mediaType = "application/json", 
                    schema = @Schema(implementation = Page.class)
                )
            ),
            @ApiResponse(
                responseCode = "401", 
                description = "Não autorizado - token JWT inválido ou ausente"
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping()
    public ResponseEntity<Page<UserResponse>> list(
            @Parameter(description = "Parâmetros de paginação (page, size, sort)")
            Pageable pageable) {
        Page<UserResponse> page = list.handle(pageable);

        return ResponseEntity.ok(page);
    }

    @Operation(
        summary = "Criar usuário", 
        description = "Registra um novo usuário no sistema. A senha deve ter pelo menos 8 caracteres."
    )
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "201", 
                description = "Usuário criado com sucesso",
                content = @Content(
                    mediaType = "application/json", 
                    schema = @Schema(implementation = UserResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400", 
                description = "Dados de entrada inválidos - formato de email inválido, senha muito curta ou campos obrigatórios ausentes"
            ),
            @ApiResponse(
                responseCode = "409", 
                description = "Conflito - email já existe no sistema"
            )
    })
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest user) {
        UserResponse created = register.handle(user.name(), user.email(), user.password());

        return ResponseEntity.created(URI.create("users/" + created.id())).body(created);
    }
}