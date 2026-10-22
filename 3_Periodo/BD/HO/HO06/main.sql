-- MySQL Workbench Forward Engineering
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ho06
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS ho06;
USE ho06;

DROP TABLE IF EXISTS TOPICO;
DROP TABLE IF EXISTS MODULO;
DROP TABLE IF EXISTS MATRICULA;
DROP TABLE IF EXISTS PROFESSOR;
DROP TABLE IF EXISTS ALUNO;
DROP TABLE IF EXISTS CURSO;
DROP TABLE IF EXISTS AREA;

-- -----------------------------------------------------
-- Table AREA
-- -----------------------------------------------------
CREATE TABLE AREA (
    sigla CHAR(6) PRIMARY KEY,
    nome VARCHAR(20) UNIQUE NOT NULL,
    superarea CHAR(6),
    FOREIGN KEY (superarea) REFERENCES AREA(sigla)
);

-- -----------------------------------------------------
-- Table CURSO
-- -----------------------------------------------------
CREATE TABLE CURSO (
    sigla CHAR(6) PRIMARY KEY,
    nome VARCHAR(50) UNIQUE NOT NULL,
    horas INT NOT NULL CHECK (horas > 0),
    custo FLOAT,
    area CHAR(6) NOT NULL,
    FOREIGN KEY (area) REFERENCES AREA(sigla)
);

-- -----------------------------------------------------
-- Table ALUNO
-- -----------------------------------------------------
CREATE TABLE ALUNO (
    cpf CHAR(11) PRIMARY KEY,
    nome VARCHAR(20) NOT NULL,
    sobrenome VARCHAR(20) NOT NULL,
    sexo CHAR(1) CHECK (sexo IN ('M','F','O')),
    dataNasc DATE NOT NULL,
    UNIQUE (nome, sobrenome)
);

-- -----------------------------------------------------
-- Table PROFESSOR
-- -----------------------------------------------------
CREATE TABLE PROFESSOR (
    cpf CHAR(11) NOT NULL,
    nome VARCHAR(100),
    curso CHAR(6),
    PRIMARY KEY (curso, cpf),
    FOREIGN KEY (curso) REFERENCES CURSO(sigla)
);

-- -----------------------------------------------------
-- Table MATRICULA
-- -----------------------------------------------------
CREATE TABLE MATRICULA (
    curso CHAR(6),
    aluno CHAR(11) NOT NULL,
    data_matricula DATE NOT NULL,
    pago BOOLEAN DEFAULT false,
    PRIMARY KEY (curso, aluno),
    FOREIGN KEY (aluno) REFERENCES ALUNO(cpf),
    FOREIGN KEY (curso) REFERENCES CURSO(sigla)
);

-- -----------------------------------------------------
-- Table MODULO
-- -----------------------------------------------------
CREATE TABLE MODULO (
    sigla VARCHAR(10) PRIMARY KEY,
    nome VARCHAR(20) UNIQUE NOT NULL,
    curso CHAR(6),
    FOREIGN KEY (curso) REFERENCES CURSO(sigla)
);

-- -----------------------------------------------------
-- Table TOPICO
-- -----------------------------------------------------
CREATE TABLE TOPICO (
    modulo VARCHAR(10),
    sigla VARCHAR(10),
    nome VARCHAR(20) UNIQUE NOT NULL,
    horas INT NOT NULL CHECK (horas > 0),
    PRIMARY KEY (modulo, sigla),
    FOREIGN KEY (modulo) REFERENCES MODULO(sigla)
);

-- Restore settings
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
