CREATE DATABASE IF NOT EXISTS CaoViver;
USE CaoViver;


-- -----------------------------------------------------
-- Table animal 
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS animal (
  numero_registro INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  sexo ENUM('M', 'F') NOT NULL,
  porte VARCHAR(50) NOT NULL,
  pelagem VARCHAR(50) NOT NULL,
  idade INT NOT NULL,
  especie VARCHAR(25) NOT NULL,
  historia TEXT DEFAULT NULL,
  castrado TINYINT(1) NOT NULL DEFAULT 0,
  testado TINYINT(1) NOT NULL DEFAULT 0,
  vermifugado TINYINT(1) NOT NULL DEFAULT 0,
  vacinado TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (numero_registro)
);




-- -----------------------------------------------------
-- Table receptor
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS receptor (
  cpf VARCHAR(11) NOT NULL,
  nome VARCHAR(100) NOT NULL,
  endereco VARCHAR(200) NOT NULL,
  data_nasc DATE NOT NULL,
  email VARCHAR(100) NOT NULL,
  telefone VARCHAR(20) NOT NULL,
  PRIMARY KEY (cpf)
);




-- -----------------------------------------------------
-- Table adocao
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS adocao (
  registro_animal INT NOT NULL,
  cpf_receptor VARCHAR(11) NOT NULL,
  data_adocao DATE NOT NULL,
  PRIMARY KEY (registro_animal, cpf_receptor),
  CONSTRAINT fk_adocao_animal
    FOREIGN KEY (registro_animal) REFERENCES animal(numero_registro) ON DELETE CASCADE,
  CONSTRAINT fk_adocao_receptor
    FOREIGN KEY (cpf_receptor) REFERENCES receptor(cpf) ON DELETE CASCADE
);




-- -----------------------------------------------------
-- Table atividade
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS atividade (
  sigla CHAR(10) NOT NULL,
  nome VARCHAR(100) NOT NULL,
  descricao TEXT DEFAULT NULL,
  PRIMARY KEY (sigla)
);




-- -----------------------------------------------------
-- Table animal_atividade
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS animal_atividade (
  registro_animal INT NOT NULL,
  sigla_atividade CHAR(10) NOT NULL,
  PRIMARY KEY (registro_animal, sigla_atividade),
  CONSTRAINT fk_animal_atividade_animal
    FOREIGN KEY (registro_animal) REFERENCES animal(numero_registro) ON DELETE CASCADE,
  CONSTRAINT fk_animal_atividade_atividade
    FOREIGN KEY (sigla_atividade) REFERENCES atividade(sigla) ON DELETE CASCADE
);




-- -----------------------------------------------------
-- Table padrinho
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS padrinho (
  cpf VARCHAR(11) NOT NULL,
  nome VARCHAR(100) NOT NULL,
  telefone VARCHAR(20) NOT NULL,
  email VARCHAR(100) NOT NULL,
  PRIMARY KEY (cpf)
);




-- -----------------------------------------------------
-- Table apadrinhamento
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS apadrinhamento (
  cpf_padrinho VARCHAR(11) NOT NULL,
  registro_animal INT NOT NULL,
  plano VARCHAR(50) NOT NULL,
  pago TINYINT(1) NOT NULL,
  PRIMARY KEY (cpf_padrinho, registro_animal),
  CONSTRAINT fk_apadrinhamento_padrinho
    FOREIGN KEY (cpf_padrinho) REFERENCES padrinho(cpf) ON DELETE CASCADE,
  CONSTRAINT fk_apadrinhamento_animal
    FOREIGN KEY (registro_animal) REFERENCES animal(numero_registro) ON DELETE CASCADE
);




-- -----------------------------------------------------
-- Table capturador
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS capturador (
  cpf VARCHAR(11) NOT NULL,
  nome VARCHAR(100) NOT NULL,
  telefone VARCHAR(20) NOT NULL,
  email VARCHAR(100) NOT NULL,
  PRIMARY KEY (cpf)
);




-- -----------------------------------------------------
-- Table captura
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS captura (
  numero_registro INT NOT NULL,
  cpf_capturador VARCHAR(11) NOT NULL,
  data_captura DATE NOT NULL,
  local_captura VARCHAR(200) NOT NULL,
  PRIMARY KEY (numero_registro, cpf_capturador),
  CONSTRAINT fk_captura_animal
    FOREIGN KEY (numero_registro) REFERENCES animal(numero_registro) ON DELETE CASCADE,
  CONSTRAINT fk_captura_capturador
    FOREIGN KEY (cpf_capturador) REFERENCES capturador(cpf) ON DELETE CASCADE
);




-- -----------------------------------------------------
-- Table veterinario
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS veterinario (
  cpf VARCHAR(11) NOT NULL,
  crmv VARCHAR(20) NOT NULL,
  crmv_estado CHAR(2) NOT NULL,
  nome VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  telefone VARCHAR(20) NOT NULL,
  especialidade VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (cpf)
);




-- -----------------------------------------------------
-- Table atendimento
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS atendimento (
  cpf_veterinario VARCHAR(11) NOT NULL,
  registro_animal INT NOT NULL,
  data_atendimento DATE NOT NULL,
  descricao TEXT DEFAULT NULL,
  PRIMARY KEY (cpf_veterinario, registro_animal),
  CONSTRAINT fk_atendimento_veterinario
    FOREIGN KEY (cpf_veterinario) REFERENCES veterinario(cpf) ON DELETE CASCADE,
  CONSTRAINT fk_atendimento_animal
    FOREIGN KEY (registro_animal) REFERENCES animal(numero_registro) ON DELETE CASCADE
);




-- -----------------------------------------------------
-- Table projeto
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS projeto (
  sigla CHAR(10) NOT NULL,
  nome VARCHAR(100) NOT NULL,
  descricao TEXT DEFAULT NULL,
  status TINYINT(1) NOT NULL,
  PRIMARY KEY (sigla)
);




-- -----------------------------------------------------
-- Table voluntario
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS voluntario (
  cpf VARCHAR(11) NOT NULL,
  nome VARCHAR(100) NOT NULL,
  telefone VARCHAR(20) NOT NULL,
  email VARCHAR(100) NOT NULL,
  idade INT NOT NULL,
  bairro VARCHAR(100) NOT NULL,
  cidade VARCHAR(100) NOT NULL,
  atividades_interesse TEXT DEFAULT NULL,
  PRIMARY KEY (cpf)
);




-- -----------------------------------------------------
-- Table voluntario_atividade
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS voluntario_atividade (
  cpf_voluntario VARCHAR(11) NOT NULL,
  sigla_atividade CHAR(10) NOT NULL,
  PRIMARY KEY (cpf_voluntario, sigla_atividade),
  CONSTRAINT fk_voluntario_atividade_voluntario
    FOREIGN KEY (cpf_voluntario) REFERENCES voluntario(cpf) ON DELETE CASCADE,
  CONSTRAINT fk_voluntario_atividade_atividade
    FOREIGN KEY (sigla_atividade) REFERENCES atividade(sigla) ON DELETE CASCADE
);




-- -----------------------------------------------------
-- Table voluntario_projeto
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS voluntario_projeto (
  cpf_voluntario VARCHAR(11) NOT NULL,
  sigla_projeto CHAR(10) NOT NULL,
  PRIMARY KEY (cpf_voluntario, sigla_projeto),
  CONSTRAINT fk_voluntario_projeto_voluntario
    FOREIGN KEY (cpf_voluntario) REFERENCES voluntario(cpf) ON DELETE CASCADE,
  CONSTRAINT fk_voluntario_projeto_projeto
    FOREIGN KEY (sigla_projeto) REFERENCES projeto(sigla) ON DELETE CASCADE
);
