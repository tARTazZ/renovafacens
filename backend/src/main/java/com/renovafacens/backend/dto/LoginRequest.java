package com.renovafacens.backend.dto;

import jakarta.validation.constraints.NotBlank;

/** JSON que o front envia em POST /api/auth/login. */
public record LoginRequest(
        @NotBlank(message = "Informe seu e-mail.")
        String email,

        @NotBlank(message = "Informe sua senha.")
        String senha
) {}
