-- =====================================================================
-- RenovaFacens - schema.sql
-- Compatível com MySQL 8 / MariaDB 10.4+
-- =====================================================================

CREATE DATABASE IF NOT EXISTS renovafacens
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE renovafacens;

-- ---------------------------------------------------------------------
-- USUARIO: estudantes, embaixadores e apoiadores
-- ---------------------------------------------------------------------
CREATE TABLE USUARIO (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  nome       VARCHAR(150) NOT NULL,
  email      VARCHAR(150) NOT NULL,
  senha_hash VARCHAR(255) NOT NULL,  -- nunca guardar senha em texto puro
  papel      ENUM('ESTUDANTE','EMBAIXADOR','APOIADOR') NOT NULL,
  criado_em  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uq_usuario_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
-- MISSAO: criada por um embaixador
-- ---------------------------------------------------------------------
CREATE TABLE MISSAO (
  id_missao              INT AUTO_INCREMENT PRIMARY KEY,
  titulo                 VARCHAR(150) NOT NULL,
  descricao              VARCHAR(500) NOT NULL,
  data_inicio            DATETIME NOT NULL,
  data_fim               DATETIME NOT NULL,
  pontos                 INT NOT NULL DEFAULT 0,
  informacoes_adicionais VARCHAR(255) NULL,
  situacao               ENUM('AGENDADA','EM_ANDAMENTO','ENCERRADA')
                         NOT NULL DEFAULT 'AGENDADA',
  id_embaixador          INT NOT NULL,
  criado_em              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_missao_pontos  CHECK (pontos >= 0),
  CONSTRAINT chk_missao_periodo CHECK (data_fim >= data_inicio),
  CONSTRAINT fk_missao_embaixador
    FOREIGN KEY (id_embaixador) REFERENCES USUARIO(id_usuario)
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
-- PARTICIPACAO: inscrição, presença validada pelo embaixador e pontos
-- ---------------------------------------------------------------------
CREATE TABLE PARTICIPACAO (
  id_participacao   INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario        INT NOT NULL,
  id_missao         INT NOT NULL,
  situacao          ENUM('INSCRITO','PRESENTE','AUSENTE')
                    NOT NULL DEFAULT 'INSCRITO',
  pontos_ganhos     INT NOT NULL DEFAULT 0,
  data_participacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_validacao    DATETIME NULL,   -- preenchida quando o embaixador valida
  CONSTRAINT uq_participacao_usuario_missao UNIQUE (id_usuario, id_missao),
  CONSTRAINT chk_participacao_pontos CHECK (pontos_ganhos >= 0),
  CONSTRAINT fk_participacao_usuario
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
    ON DELETE RESTRICT,
  CONSTRAINT fk_participacao_missao
    FOREIGN KEY (id_missao) REFERENCES MISSAO(id_missao)
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
-- RECOMPENSA: prêmios/brindes oferecidos por apoiadores
-- ---------------------------------------------------------------------
CREATE TABLE RECOMPENSA (
  id_recompensa INT AUTO_INCREMENT PRIMARY KEY,
  nome          VARCHAR(150) NOT NULL,
  descricao     VARCHAR(500) NULL,
  custo_pontos  INT NOT NULL,
  estoque       INT NOT NULL DEFAULT 0,
  situacao      ENUM('ATIVA','INATIVA') NOT NULL DEFAULT 'ATIVA',
  id_apoiador   INT NOT NULL,
  criado_em     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_recompensa_custo   CHECK (custo_pontos > 0),
  CONSTRAINT chk_recompensa_estoque CHECK (estoque >= 0),
  CONSTRAINT fk_recompensa_apoiador
    FOREIGN KEY (id_apoiador) REFERENCES USUARIO(id_usuario)
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
-- RESGATE: troca de pontos por recompensa
-- pontos_gastos guarda o custo no momento do resgate (histórico)
-- ---------------------------------------------------------------------
CREATE TABLE RESGATE (
  id_resgate    INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario    INT NOT NULL,
  id_recompensa INT NOT NULL,
  pontos_gastos INT NOT NULL,
  data_resgate  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_resgate_pontos CHECK (pontos_gastos > 0),
  CONSTRAINT fk_resgate_usuario
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
    ON DELETE RESTRICT,
  CONSTRAINT fk_resgate_recompensa
    FOREIGN KEY (id_recompensa) REFERENCES RECOMPENSA(id_recompensa)
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---------------------------------------------------------------------
-- Saldo de pontos = pontos de presenças validadas - pontos gastos em resgates
-- Usado por GET /api/usuarios/{id}/pontos
-- ---------------------------------------------------------------------
CREATE OR REPLACE VIEW VW_SALDO_PONTOS AS
SELECT
  u.id_usuario,
  COALESCE((SELECT SUM(p.pontos_ganhos)
              FROM PARTICIPACAO p
             WHERE p.id_usuario = u.id_usuario
               AND p.situacao = 'PRESENTE'), 0)
  - COALESCE((SELECT SUM(r.pontos_gastos)
                FROM RESGATE r
               WHERE r.id_usuario = u.id_usuario), 0) AS saldo_pontos
FROM USUARIO u;
