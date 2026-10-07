package com.renovafacens.backend.model;

/** Representa uma linha da tabela USUARIO (mais o total de pontos, calculado). */
public class Usuario {
    private Long idUsuario;
    private String nome;
    private String email;
    private String senhaHash;
    private Papel papel;
    private int pontos;

    public Usuario() {}

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public Papel getPapel() { return papel; }
    public void setPapel(Papel papel) { this.papel = papel; }

    public int getPontos() { return pontos; }
    public void setPontos(int pontos) { this.pontos = pontos; }
}
