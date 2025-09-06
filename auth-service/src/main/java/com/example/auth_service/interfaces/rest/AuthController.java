package com.example.auth_service.interfaces.rest;

import com.example.auth_service.application.auth.LogoutHandler;
import com.example.auth_service.application.auth.PasswordLoginHandler;
import com.example.auth_service.application.auth.RequestMagicLinkHandler;
import com.example.auth_service.application.auth.VerifyMagicLinkHandler;
import com.example.auth_service.interfaces.rest.dto.auth.MagicLinkRequest;
import com.example.auth_service.interfaces.rest.dto.auth.MagicLinkVerifyRequest;
import com.example.auth_service.application.auth.RefreshTokenHandler;
import com.example.auth_service.interfaces.rest.dto.auth.LogoutRequest;
import com.example.auth_service.interfaces.rest.dto.auth.PasswordLoginRequest;
import com.example.auth_service.interfaces.rest.dto.auth.RefreshTokenRequest;
import com.example.auth_service.interfaces.rest.dto.auth.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints para autenticação de usuários")
public class AuthController {
    private final PasswordLoginHandler passwordLoginHandler;
    private final RequestMagicLinkHandler requestMagicLinkHandler;
    private final VerifyMagicLinkHandler verifyMagicLinkHandler;
    private final RefreshTokenHandler refreshTokenHandler;
    private final LogoutHandler logoutHandler;

    @PostMapping("/login/password")
    public ResponseEntity<TokenResponse> loginWithPassword(@Valid @RequestBody PasswordLoginRequest request) {
        TokenResponse token = passwordLoginHandler.handle(request.email(), request.password());

        return ResponseEntity.ok(token);
    }

    @PostMapping("/login/magic")
    public ResponseEntity<Void> requestMagic(@Valid @RequestBody MagicLinkRequest req) {
        requestMagicLinkHandler.handle(req.email());

        return ResponseEntity.accepted().build();
    }

    @PostMapping("login/magic/verify")
    public ResponseEntity<TokenResponse> verifyMagic(@Valid @RequestBody MagicLinkVerifyRequest request) {
        TokenResponse tokenResponse = verifyMagicLinkHandler.handle(request.token());

        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(
        summary = "Renovar token de acesso", 
        description = "Renova o token de acesso usando um refresh token válido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200", 
                description = "Token renovado com sucesso",
                content = @Content(
                    mediaType = "application/json", 
                    schema = @Schema(implementation = TokenResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401", 
                description = "Refresh token inválido ou expirado"
            ),
            @ApiResponse(
                responseCode = "400", 
                description = "Dados de entrada inválidos - refresh token ausente"
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse token = refreshTokenHandler.handle(request.refreshToken());
        return ResponseEntity.ok(token);
    }

    @Operation(
        summary = "Logout do usuário", 
        description = "Revoga o refresh token, invalidando a sessão do usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200", 
                description = "Logout realizado com sucesso"
            ),
            @ApiResponse(
                responseCode = "400", 
                description = "Dados de entrada inválidos - refresh token ausente"
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        logoutHandler.handle(request.refreshToken());
        return ResponseEntity.ok().build();
    }
}
