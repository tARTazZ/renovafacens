package com.renovafacens.backend.exception;

import org.springframework.http.HttpStatus;

/** Erro "esperado" (regra de negócio). Vira uma resposta HTTP com o status escolhido. */
public class ApiException extends RuntimeException {
    private final HttpStatus status;

    public ApiException(HttpStatus status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    public HttpStatus getStatus() { return status; }
}
