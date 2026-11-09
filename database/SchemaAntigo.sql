CREATE DATABASE IF NOT EXISTS renovafacens
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE renovafacens;

CREATE TABLE USUARIO (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  nome       VARCHAR(100) NOT NULL,
  email      VARCHAR(150) NOT NULL UNIQUE,
  senha_hash VARCHAR(255) NOT NULL,
  papel      ENUM('ESTUDANTE','EMBAIXADOR') NOT NULL
);

CREATE TABLE MISSAO (
  id_missao             INT AUTO_INCREMENT PRIMARY KEY,
  titulo                VARCHAR(150) NOT NULL,
  descricao             TEXT NOT NULL,
  data_inicio           DATETIME NOT NULL,
  data_termino          DATETIME NOT NULL,
  pontos                INT NOT NULL DEFAULT 0,
  informacoes_adicionais TEXT,
  status                VARCHAR(20),
  id_embaixador         INT NOT NULL,
  FOREIGN KEY (id_embaixador) REFERENCES USUARIO(id_usuario)
);

CREATE TABLE PARTICIPACAO (
  id_participacao   INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario        INT NOT NULL,
  id_missao         INT NOT NULL,
  status            VARCHAR(20) NOT NULL DEFAULT 'inscrito',
  data_participacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  pontos_ganhos     INT NOT NULL DEFAULT 0,
  UNIQUE (id_usuario, id_missao),
  FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario),
  FOREIGN KEY (id_missao)  REFERENCES MISSAO(id_missao)
);
