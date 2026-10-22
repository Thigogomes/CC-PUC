-- -----------------------------------------------------
-- Inserindo animais
-- -----------------------------------------------------
INSERT INTO animal (nome, sexo, porte, pelagem, idade, especie, historia, castrado, testado, vermifugado, vacinado)
VALUES 
('Rex', 'M', 'Médio', 'Curta', 3, 'Cachorro', 'Encontrado nas ruas da cidade.', 1, 1, 1, 1),
('Luna', 'F', 'Pequeno', 'Longa', 2, 'Cachorro', 'Resgatada de um abrigo.', 0, 1, 1, 1),
('Mingau', 'M', 'Pequeno', 'Curta', 1, 'Gato', 'Filhote abandonado.', 0, 0, 0, 0);

-- -----------------------------------------------------
-- Inserindo receptores
-- -----------------------------------------------------
INSERT INTO receptor (cpf, nome, endereco, data_nasc, email, telefone)
VALUES
('12345678901', 'Ana Silva', 'Rua A, 123', '1985-06-15', 'ana@email.com', '11999999999'),
('23456789012', 'Carlos Souza', 'Rua B, 456', '1990-03-22', 'carlos@email.com', '11988888888');

-- -----------------------------------------------------
-- Inserindo adoções
-- -----------------------------------------------------
INSERT INTO adocao (registro_animal, cpf_receptor, data_adocao)
VALUES
(1, '12345678901', '2025-01-10'),
(2, '23456789012', '2025-02-05');

-- -----------------------------------------------------
-- Inserindo atividades
-- -----------------------------------------------------
INSERT INTO atividade (sigla, nome, descricao)
VALUES
('SOC', 'Socialização', 'Atividades para socializar os animais.'),
('TRE', 'Treinamento', 'Treinamento de obediência e comportamento.');

-- -----------------------------------------------------
-- Inserindo vínculo animal_atividade
-- -----------------------------------------------------
INSERT INTO animal_atividade (registro_animal, sigla_atividade)
VALUES
(1, 'SOC'),
(2, 'TRE');

-- -----------------------------------------------------
-- Inserindo padrinhos
-- -----------------------------------------------------
INSERT INTO padrinho (cpf, nome, telefone, email)
VALUES
('34567890123', 'Mariana Lima', '11977777777', 'mariana@email.com');

-- -----------------------------------------------------
-- Inserindo apadrinhamento
-- -----------------------------------------------------
INSERT INTO apadrinhamento (cpf_padrinho, registro_animal, plano, pago)
VALUES
('34567890123', 3, 'Mensal', 1);

-- -----------------------------------------------------
-- Inserindo capturadores
-- -----------------------------------------------------
INSERT INTO capturador (cpf, nome, telefone, email)
VALUES
('45678901234', 'João Pereira', '11966666666', 'joao@email.com');

-- -----------------------------------------------------
-- Inserindo captura
-- -----------------------------------------------------
INSERT INTO captura (numero_registro, cpf_capturador, data_captura, local_captura)
VALUES
(3, '45678901234', '2025-03-15', 'Parque Central');

-- -----------------------------------------------------
-- Inserindo veterinários
-- -----------------------------------------------------
INSERT INTO veterinario (cpf, crmv, crmv_estado, nome, email, telefone, especialidade)
VALUES
('56789012345', 'CRMV1234', 'SP', 'Dra. Paula', 'paula@email.com', '11955555555', 'Clínica Geral');

-- -----------------------------------------------------
-- Inserindo atendimentos
-- -----------------------------------------------------
INSERT INTO atendimento (cpf_veterinario, registro_animal, data_atendimento, descricao)
VALUES
('56789012345', 1, '2025-01-11', 'Consulta de rotina e vacinação.');

-- -----------------------------------------------------
-- Inserindo projetos
-- -----------------------------------------------------
INSERT INTO projeto (sigla, nome, descricao, status)
VALUES
('ADO', 'Adoção Responsável', 'Projeto para promover adoção de animais.', 1);

-- -----------------------------------------------------
-- Inserindo voluntários
-- -----------------------------------------------------
INSERT INTO voluntario (cpf, nome, telefone, email, idade, bairro, cidade, atividades_interesse)
VALUES
('67890123456', 'Lucas Ferreira', '11944444444', 'lucas@email.com', 25, 'Centro', 'São Paulo', 'SOC,TRE');

-- -----------------------------------------------------
-- Inserindo vínculo voluntário_atividade
-- -----------------------------------------------------
INSERT INTO voluntario_atividade (cpf_voluntario, sigla_atividade)
VALUES
('67890123456', 'SOC'),
('67890123456', 'TRE');

-- -----------------------------------------------------
-- Inserindo vínculo voluntário_projeto
-- -----------------------------------------------------
INSERT INTO voluntario_projeto (cpf_voluntario, sigla_projeto)
VALUES
('67890123456', 'ADO');

