package com.example.auth_service.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health Check", description = "Endpoints para verificação de saúde da aplicação")
public class HelloController {

    @Operation(
        summary = "Hello World", 
        description = "Endpoint simples para testar se a aplicação está funcionando corretamente"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Mensagem de saudação retornada com sucesso"
    )
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}