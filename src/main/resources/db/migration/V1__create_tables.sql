-- Criação da tabela de status_item
CREATE TABLE IF NOT EXISTS status_item (
    id_status SERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP
);

-- Criação da tabela de administradores
CREATE TABLE IF NOT EXISTS administradores (
    id_adm SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo_de_usuario VARCHAR(50) NOT NULL,
    situacao VARCHAR(50) NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP
);

-- Criação da tabela de clientes
CREATE TABLE IF NOT EXISTS clientes (
    id_cliente SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    cpf VARCHAR(14) NOT NULL UNIQUE,
    tipo_de_usuario VARCHAR(50) NOT NULL,
    situacao VARCHAR(50) NOT NULL,
    endereco VARCHAR(255),
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP
);

-- Criação da tabela de mecanicos
CREATE TABLE IF NOT EXISTS mecanicos (
    id_mecanico SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    cpf VARCHAR(14) NOT NULL UNIQUE,
    tipo_de_usuario VARCHAR(50) NOT NULL,
    situacao VARCHAR(50) NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP
);

-- Criação da tabela de veiculos
CREATE TABLE IF NOT EXISTS veiculos (
    id_veiculo SERIAL PRIMARY KEY,
    placa VARCHAR(10) NOT NULL UNIQUE,
    marca VARCHAR(100),
    modelo VARCHAR(100),
    ano INTEGER,
    cor VARCHAR(50),
    quilometragem FLOAT,
    cliente_id INTEGER NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id_cliente)
);

-- Criação da tabela de checklists
CREATE TABLE IF NOT EXISTS checklists (
    id_checklist SERIAL PRIMARY KEY,
    id_veiculo INTEGER NOT NULL,
    id_mecanico INTEGER,
    quilometragem FLOAT,
    id_status INTEGER NOT NULL,
    descricao TEXT,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    FOREIGN KEY (id_veiculo) REFERENCES veiculos(id_veiculo),
    FOREIGN KEY (id_mecanico) REFERENCES mecanicos(id_mecanico),
    FOREIGN KEY (id_status) REFERENCES status_item(id_status)
);

-- Criação da tabela de item
CREATE TABLE IF NOT EXISTS item (
    id_item SERIAL PRIMARY KEY,
    nome_item VARCHAR(255) NOT NULL,
    parte_do_veiculo VARCHAR(100),
    imagem_ilustrativa VARCHAR(255),
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP
);

-- Criação da tabela de item_checklist
CREATE TABLE IF NOT EXISTS item_checklist (
    id_item_checklist SERIAL PRIMARY KEY,
    id_checklist INTEGER NOT NULL,
    id_item INTEGER NOT NULL,
    id_status_item INTEGER NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    FOREIGN KEY (id_checklist) REFERENCES checklists(id_checklist),
    FOREIGN KEY (id_item) REFERENCES item(id_item),
    FOREIGN KEY (id_status_item) REFERENCES status_item(id_status)
);

-- Criação da tabela de foto_evidencia
CREATE TABLE IF NOT EXISTS foto_evidencia (
    id_foto_evidencia SERIAL PRIMARY KEY,
    id_item_checklist INTEGER NOT NULL,
    url_foto VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    FOREIGN KEY (id_item_checklist) REFERENCES item_checklist(id_item_checklist)
);

-- Criação da tabela de servico
CREATE TABLE IF NOT EXISTS servico (
    id_servico SERIAL PRIMARY KEY,
    id_checklist INTEGER NOT NULL,
    id_mecanico INTEGER NOT NULL,
    data_realizacao DATE,
    id_status INTEGER NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    FOREIGN KEY (id_checklist) REFERENCES checklists(id_checklist),
    FOREIGN KEY (id_mecanico) REFERENCES mecanicos(id_mecanico),
    FOREIGN KEY (id_status) REFERENCES status_item(id_status)
);

-- Criação da tabela de orcamento
CREATE TABLE IF NOT EXISTS orcamento (
    id_orcamento SERIAL PRIMARY KEY,
    id_servico INTEGER NOT NULL,
    valor_total FLOAT,
    data_emissao DATE,
    data_aprovacao DATE,
    id_status INTEGER NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    FOREIGN KEY (id_servico) REFERENCES servico(id_servico),
    FOREIGN KEY (id_status) REFERENCES status_item(id_status)
);

-- Criação da tabela de item_orcamento
CREATE TABLE IF NOT EXISTS item_orcamento (
    id_item_orcamento SERIAL PRIMARY KEY,
    id_orcamento INTEGER NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    quantidade INTEGER NOT NULL,
    valor_unitario FLOAT NOT NULL,
    valor_total FLOAT NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    FOREIGN KEY (id_orcamento) REFERENCES orcamento(id_orcamento)
);

-- Criação da tabela de pagamento
CREATE TABLE IF NOT EXISTS pagamento (
    id_pagamento SERIAL PRIMARY KEY,
    id_orcamento INTEGER NOT NULL,
    valor FLOAT NOT NULL,
    data_pagamento DATE,
    metodo_pagamento VARCHAR(100),
    id_status INTEGER NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    FOREIGN KEY (id_orcamento) REFERENCES orcamento(id_orcamento),
    FOREIGN KEY (id_status) REFERENCES status_item(id_status)
);

-- Inserção de dados iniciais na tabela status_item
INSERT INTO status_item (descricao, criado_em, atualizado_em) VALUES
('Pendente', NOW(), NOW()),
('Em Andamento', NOW(), NOW()),
('Concluído', NOW(), NOW()),
('Cancelado', NOW(), NOW()),
('Aprovado', NOW(), NOW()),
('Reprovado', NOW(), NOW()),
('Pago', NOW(), NOW()),
('Não Pago', NOW(), NOW());