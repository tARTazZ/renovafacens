package com.renovafacens.backend.dto;

import com.renovafacens.backend.model.Usuario;

/** O que a API devolve sobre um usuário. Repare: NÃO tem senha nem hash. */
public record UsuarioResponse(
        Long idUsuario,
        String nome,
        String email,
        String papel,
        int pontos
) {
    public static UsuarioResponse de(Usuario u) {
        return new UsuarioResponse(u.getIdUsuario(), u.getNome(), u.getEmail(), u.getPapel().name(), u.getPontos());
    }
}
