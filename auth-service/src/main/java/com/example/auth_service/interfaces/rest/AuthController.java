package com.example.auth_service.interfaces.rest;

import com.example.auth_service.application.auth.PasswordLoginHandler;
import com.example.auth_service.interfaces.rest.dto.auth.PasswordLoginRequest;
import com.example.auth_service.interfaces.rest.dto.auth.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private PasswordLoginHandler passwordLoginHandler;

    public ResponseEntity<TokenResponse> loginWithPassword(@Valid @RequestBody PasswordLoginRequest request) {
        TokenResponse token = passwordLoginHandler.handle(request.email(), request.password());

        return ResponseEntity.ok(token);
    }
}
