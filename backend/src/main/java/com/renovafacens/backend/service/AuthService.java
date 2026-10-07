package com.renovafacens.backend.service;

import com.renovafacens.backend.dto.CadastroRequest;
import com.renovafacens.backend.dto.LoginRequest;
import com.renovafacens.backend.dto.UsuarioResponse;
import com.renovafacens.backend.exception.ApiException;
import com.renovafacens.backend.model.Papel;
import com.renovafacens.backend.model.Usuario;
import com.renovafacens.backend.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Regras de negócio de cadastro e login (porte de cadastrarUsuario/autenticar do dados.js). */
@Service
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public AuthService(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public UsuarioResponse cadastrar(CadastroRequest req) {
        String nome = req.nome().trim();
        String email = req.email().trim().toLowerCase();

        if (nome.split("\\s+").length < 2) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Digite nome e sobrenome.");
        }

        Papel papel = Papel.deTexto(req.papel());
        if (papel == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Escolha Estudante ou Embaixador.");
        }

        if (repository.existePorEmail(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "Este e-mail já está cadastrado.");
        }

        String hash = encoder.encode(req.senha());
        Long id = repository.inserir(nome, email, hash, papel);

        return new UsuarioResponse(id, nome, email, papel.name(), 0);
    }

    public UsuarioResponse login(LoginRequest req) {
        String email = req.email().trim().toLowerCase();

        // Mesma mensagem para "e-mail não existe" e "senha errada",
        // para não revelar quais e-mails estão cadastrados.
        Usuario usuario = repository.buscarPorEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos."));

        if (!encoder.matches(req.senha(), usuario.getSenhaHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos.");
        }

        return UsuarioResponse.de(usuario);
    }
}
