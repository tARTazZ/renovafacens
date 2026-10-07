package com.renovafacens.backend.controller;

import com.renovafacens.backend.dto.CadastroRequest;
import com.renovafacens.backend.dto.LoginRequest;
import com.renovafacens.backend.dto.UsuarioResponse;
import com.renovafacens.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rotas de autenticação. O controller só recebe a requisição, chama o Service
 * e devolve a resposta. Nenhuma regra de negócio fica aqui.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    // POST /api/auth/cadastro  ->  201 Created + usuário criado
    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody CadastroRequest req) {
        UsuarioResponse criado = service.cadastrar(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    // POST /api/auth/login  ->  200 OK + usuário logado
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(service.login(req));
    }
}
