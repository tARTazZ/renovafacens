package com.renovafacens.backend.repository;

import com.renovafacens.backend.model.Papel;
import com.renovafacens.backend.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

/**
 * Acesso ao banco via JDBC (JdbcTemplate = JDBC com a parte repetitiva já pronta).
 *
 * ATENÇÃO: os nomes abaixo (tabelas USUARIO / PARTICIPACAO e colunas id_usuario,
 * nome, email, senha_hash, papel, pontos_ganhos) seguem as convenções do README.
 * Se o SQL do grupo tiver nome diferente, ajuste SÓ as strings neste arquivo.
 */
@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbc;

    public UsuarioRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Converte uma linha do banco em objeto Usuario.
    private static final RowMapper<Usuario> MAPPER = (rs, numeroLinha) -> {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getLong("id_usuario"));
        u.setNome(rs.getString("nome"));
        u.setEmail(rs.getString("email"));
        u.setSenhaHash(rs.getString("senha_hash"));
        u.setPapel(Papel.valueOf(rs.getString("papel").toUpperCase()));
        u.setPontos(rs.getInt("pontos"));
        return u;
    };

    /** Busca por e-mail. Os pontos são a soma dos pontos_ganhos nas participações. */
    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = """
                SELECT u.id_usuario, u.nome, u.email, u.senha_hash, u.papel,
                       (SELECT COALESCE(SUM(p.pontos_ganhos), 0)
                          FROM PARTICIPACAO p
                         WHERE p.id_usuario = u.id_usuario) AS pontos
                  FROM USUARIO u
                 WHERE LOWER(u.email) = ?
                """;
        return jdbc.query(sql, MAPPER, email.toLowerCase()).stream().findFirst();
    }

    public boolean existePorEmail(String email) {
        Integer total = jdbc.queryForObject(
                "SELECT COUNT(*) FROM USUARIO WHERE LOWER(email) = ?",
                Integer.class,
                email.toLowerCase());
        return total != null && total > 0;
    }

    /** Insere o usuário e devolve o id gerado pelo banco (AUTO_INCREMENT). */
    public Long inserir(String nome, String email, String senhaHash, Papel papel) {
        String sql = "INSERT INTO USUARIO (nome, email, senha_hash, papel) VALUES (?, ?, ?, ?)";
        KeyHolder chave = new GeneratedKeyHolder();

        jdbc.update(conexao -> {
            PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, senhaHash);
            ps.setString(4, papel.name());
            return ps;
        }, chave);

        return chave.getKey().longValue();
    }
}
