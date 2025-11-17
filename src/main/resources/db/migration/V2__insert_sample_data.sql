-- Inserir administrador
INSERT INTO administradores (nome, email, senha, tipo_de_usuario, situacao, criado_em, atualizado_em)
VALUES ('Admin', 'admin@meclist.com', '$2a$10$X7.H/G7ArsOE7MCe6YrRSO.0Jo8wr.VY7.T9cRWAJsCpDVpmt682i', 'ADMIN', 'ATIVO', NOW(), NOW());

-- Inserir mecânico
INSERT INTO mecanicos (nome, email, senha, telefone, cpf, tipo_de_usuario, situacao, criado_em, atualizado_em)
VALUES ('João Mecânico', 'joao@meclist.com', '$2a$10$X7.H/G7ArsOE7MCe6YrRSO.0Jo8wr.VY7.T9cRWAJsCpDVpmt682i', '11999999999', '12345678901', 'MECANICO', 'ATIVO', NOW(), NOW());

-- Inserir cliente
INSERT INTO clientes (nome, email, senha, telefone, cpf, tipo_de_usuario, situacao, endereco, criado_em, atualizado_em)
VALUES ('Maria Cliente', 'maria@email.com', '$2a$10$X7.H/G7ArsOE7MCe6YrRSO.0Jo8wr.VY7.T9cRWAJsCpDVpmt682i', '11988888888', '98765432101', 'CLIENTE', 'ATIVO', 'Rua das Flores, 123', NOW(), NOW());

-- Compat: se a PK for 'id' (legado), renomeia para 'id_cliente' antes de usar
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'clientes' AND column_name = 'id'
    ) AND NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'clientes' AND column_name = 'id_cliente'
    ) THEN
        EXECUTE 'ALTER TABLE clientes RENAME COLUMN id TO id_cliente';
    END IF;
END$$;

-- Inserir veículo (associando ao cliente pela chave natural - email)
INSERT INTO veiculos (placa, marca, modelo, ano, cor, quilometragem, cliente_id, criado_em, atualizado_em)
VALUES (
    'ABC1234', 'Toyota', 'Corolla', 2020, 'Prata', 15000,
    (SELECT id_cliente FROM clientes WHERE email = 'maria@email.com'),
    NOW(), NOW()
);

-- Inserir itens
INSERT INTO item (nome_item, parte_do_veiculo, imagem_ilustrativa, criado_em, atualizado_em) VALUES
('Óleo do motor', 'CAPO_LEVANTADO', 'oleo_motor.jpg', NOW(), NOW()),
('Filtro de ar', 'CAPO_LEVANTADO', 'filtro_ar.jpg', NOW(), NOW()),
('Pastilhas de freio', 'FORA_DO_VEICULO', 'pastilhas_freio.jpg', NOW(), NOW()),
('Fluido de freio', 'FORA_DO_VEICULO', 'fluido_freio.jpg', NOW(), NOW()),
('Amortecedores', 'VEICULO_NO_ELEVADOR', 'amortecedores.jpg', NOW(), NOW()),
('Bateria', 'CAPO_LEVANTADO', 'bateria.jpg', NOW(), NOW()),
('Lâmpadas', 'FORA_DO_VEICULO', 'lampadas.jpg', NOW(), NOW()),
('Pneus', 'VEICULO_NO_CHAO', 'pneus.jpg', NOW(), NOW());

-- Compatibilidade: corrigir nomes de PK geradas pelo Hibernate em ambientes legados
DO $$
BEGIN
    -- veiculos: id -> id_veiculo
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'veiculos' AND column_name = 'id'
    ) AND NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'veiculos' AND column_name = 'id_veiculo'
    ) THEN
        EXECUTE 'ALTER TABLE veiculos RENAME COLUMN id TO id_veiculo';
    END IF;

    -- mecanicos: id -> id_mecanico
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'mecanicos' AND column_name = 'id'
    ) AND NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'mecanicos' AND column_name = 'id_mecanico'
    ) THEN
        EXECUTE 'ALTER TABLE mecanicos RENAME COLUMN id TO id_mecanico';
    END IF;

    -- status_item: id -> id_status
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'status_item' AND column_name = 'id'
    ) AND NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'status_item' AND column_name = 'id_status'
    ) THEN
        EXECUTE 'ALTER TABLE status_item RENAME COLUMN id TO id_status';
    END IF;

    -- item: id -> id_item
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'item' AND column_name = 'id'
    ) AND NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'item' AND column_name = 'id_item'
    ) THEN
        EXECUTE 'ALTER TABLE item RENAME COLUMN id TO id_item';
    END IF;

    -- checklists: id -> id_checklist
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'checklists' AND column_name = 'id'
    ) AND NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'checklists' AND column_name = 'id_checklist'
    ) THEN
        EXECUTE 'ALTER TABLE checklists RENAME COLUMN id TO id_checklist';
    END IF;
END$$;

-- Correção: remover coluna 'status' indevida criada em ambientes antigos
ALTER TABLE checklists DROP COLUMN IF EXISTS status;

-- Inserir checklist usando chaves naturais para obter os IDs corretos
WITH v AS (
    SELECT id_veiculo FROM veiculos WHERE placa = 'ABC1234'
), m AS (
    SELECT id_mecanico FROM mecanicos WHERE email = 'joao@meclist.com'
), s AS (
    SELECT id_status FROM status_item WHERE descricao = 'Pendente'
)
INSERT INTO checklists (id_veiculo, id_mecanico, quilometragem, id_status, descricao, criado_em, atualizado_em)
SELECT v.id_veiculo, m.id_mecanico, 15000, s.id_status, 'Revisão de rotina', NOW(), NOW()
FROM v, m, s;

-- Inserir itens do checklist
-- Inserir itens do checklist utilizando subselects seguros
INSERT INTO item_checklist (id_checklist, id_item, id_status_item, criado_em, atualizado_em)
VALUES
((SELECT id_checklist FROM checklists WHERE descricao = 'Revisão de rotina' ORDER BY id_checklist DESC LIMIT 1),
 (SELECT id_item FROM item WHERE nome_item = 'Óleo do motor'),
 (SELECT id_status FROM status_item WHERE descricao = 'Pendente'), NOW(), NOW()),
((SELECT id_checklist FROM checklists WHERE descricao = 'Revisão de rotina' ORDER BY id_checklist DESC LIMIT 1),
 (SELECT id_item FROM item WHERE nome_item = 'Filtro de ar'),
 (SELECT id_status FROM status_item WHERE descricao = 'Pendente'), NOW(), NOW()),
((SELECT id_checklist FROM checklists WHERE descricao = 'Revisão de rotina' ORDER BY id_checklist DESC LIMIT 1),
 (SELECT id_item FROM item WHERE nome_item = 'Pastilhas de freio'),
 (SELECT id_status FROM status_item WHERE descricao = 'Pendente'), NOW(), NOW()),
((SELECT id_checklist FROM checklists WHERE descricao = 'Revisão de rotina' ORDER BY id_checklist DESC LIMIT 1),
 (SELECT id_item FROM item WHERE nome_item = 'Fluido de freio'),
 (SELECT id_status FROM status_item WHERE descricao = 'Pendente'), NOW(), NOW()),
((SELECT id_checklist FROM checklists WHERE descricao = 'Revisão de rotina' ORDER BY id_checklist DESC LIMIT 1),
 (SELECT id_item FROM item WHERE nome_item = 'Amortecedores'),
 (SELECT id_status FROM status_item WHERE descricao = 'Pendente'), NOW(), NOW()),
((SELECT id_checklist FROM checklists WHERE descricao = 'Revisão de rotina' ORDER BY id_checklist DESC LIMIT 1),
 (SELECT id_item FROM item WHERE nome_item = 'Bateria'),
 (SELECT id_status FROM status_item WHERE descricao = 'Pendente'), NOW(), NOW()),
((SELECT id_checklist FROM checklists WHERE descricao = 'Revisão de rotina' ORDER BY id_checklist DESC LIMIT 1),
 (SELECT id_item FROM item WHERE nome_item = 'Lâmpadas'),
 (SELECT id_status FROM status_item WHERE descricao = 'Pendente'), NOW(), NOW()),
((SELECT id_checklist FROM checklists WHERE descricao = 'Revisão de rotina' ORDER BY id_checklist DESC LIMIT 1),
 (SELECT id_item FROM item WHERE nome_item = 'Pneus'),
 (SELECT id_status FROM status_item WHERE descricao = 'Pendente'), NOW(), NOW());

-- Inserir foto de evidência
INSERT INTO foto_evidencia (id_item_checklist, url_foto, criado_em, atualizado_em)
VALUES (1, 'https://example.com/fotos/oleo_motor_123.jpg', NOW(), NOW());

-- Inserir serviço
INSERT INTO servico (id_checklist, id_mecanico, data_realizacao, id_status, criado_em, atualizado_em)
VALUES (1, 1, NOW(), 2, NOW(), NOW());

-- Inserir orçamento
INSERT INTO orcamento (id_servico, valor_total, data_emissao, id_status, criado_em, atualizado_em)
VALUES (1, 500.00, NOW(), 1, NOW(), NOW());

-- Inserir itens do orçamento
INSERT INTO item_orcamento (id_orcamento, descricao, quantidade, valor_unitario, valor_total, criado_em, atualizado_em) VALUES
(1, 'Troca de óleo', 1, 150.00, 150.00, NOW(), NOW()),
(1, 'Filtro de ar', 1, 50.00, 50.00, NOW(), NOW()),
(1, 'Mão de obra', 1, 300.00, 300.00, NOW(), NOW());

-- Inserir pagamento
INSERT INTO pagamento (id_orcamento, valor, data_pagamento, metodo_pagamento, id_status, criado_em, atualizado_em)
VALUES (1, 500.00, NULL, 'Cartão de Crédito', 8, NOW(), NOW());