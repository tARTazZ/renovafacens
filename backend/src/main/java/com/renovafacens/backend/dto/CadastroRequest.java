package com.renovafacens.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** JSON que o front envia em POST /api/auth/cadastro. */
public record CadastroRequest(
        @NotBlank(message = "Informe seu nome completo.")
        String nome,

        @NotBlank(message = "Informe seu e-mail.")
        @Email(message = "Digite um e-mail válido.")
        String email,

        @NotBlank(message = "Crie uma senha.")
        @Size(min = 6, message = "Use pelo menos 6 caracteres.")
        String senha,

        // O front atual manda "tipoDesejado"; aceitamos os dois nomes.
        @JsonAlias("tipoDesejado")
        @NotBlank(message = "Escolha Estudante ou Embaixador.")
        String papel
) {}
