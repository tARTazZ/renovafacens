package com.renovafacens.backend.model;

/**
 * Papéis do sistema. É o ÚNICO lugar que define os nomes.
 * O front antigo usava "usuario"/"mentor"; o MVP fala em Estudante/Embaixador.
 * Por isso aceitamos os dois nomes na entrada, mas guardamos sempre ESTUDANTE ou EMBAIXADOR.
 */
public enum Papel {
    ESTUDANTE,
    EMBAIXADOR;

    /** Converte o texto vindo do front. Devolve null se não reconhecer. */
    public static Papel deTexto(String texto) {
        if (texto == null) return null;
        switch (texto.trim().toLowerCase()) {
            case "estudante":
            case "usuario":
            case "usuário":
                return ESTUDANTE;
            case "embaixador":
            case "mentor":
                return EMBAIXADOR;
            default:
                return null;
        }
    }
}
