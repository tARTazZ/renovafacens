package com.renovafacens.backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/** Centraliza o tratamento de erros: todo erro vira {"erro": "mensagem"}. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> tratarApi(ApiException e) {
        return ResponseEntity.status(e.getStatus()).body(Map.of("erro", e.getMessage()));
    }

    // Falhou @Valid (campo vazio, e-mail inválido, senha curta...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(MethodArgumentNotValidException e) {
        String mensagem = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(erro -> erro.getDefaultMessage())
                .orElse("Dados inválidos.");
        return ResponseEntity.badRequest().body(Map.of("erro", mensagem));
    }

    // JSON malformado ou corpo ausente
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> tratarJsonInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", "JSON inválido ou ausente."));
    }

    // Problema com o banco (fora do ar, tabela/coluna com nome errado...)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, String>> tratarBanco(DataAccessException e) {
        log.error("Erro de banco de dados", e); // o motivo real aparece no console do IntelliJ
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("erro", "Erro ao acessar o banco de dados."));
    }
}
