-- Inserir administrador
INSERT INTO administradores (nome, email, senha, tipo_de_usuario, situacao, criado_em, atualizado_em)
VALUES ('Admin', 'admin@meclist.com', '$2a$10$X7.H/G7ArsOE7MCe6YrRSO.0Jo8wr.VY7.T9cRWAJsCpDVpmt682i', 'ADMIN', 'ATIVO', NOW(), NOW());

-- Inserir mecânico
INSERT INTO mecanicos (nome, email, senha, telefone, cpf, tipo_de_usuario, situacao, criado_em, atualizado_em)
VALUES ('João Mecânico', 'joao@meclist.com', '$2a$10$X7.H/G7ArsOE7MCe6YrRSO.0Jo8wr.VY7.T9cRWAJsCpDVpmt682i', '11999999999', '12345678901', 'MECANICO', 'ATIVO', NOW(), NOW());

-- Inserir cliente
INSERT INTO clientes (nome, email, senha, telefone, cpf, tipo_de_usuario, situacao, endereco, criado_em, atualizado_em)
VALUES ('Maria Cliente', 'maria@email.com', '$2a$10$X7.H/G7ArsOE7MCe6YrRSO.0Jo8wr.VY7.T9cRWAJsCpDVpmt682i', '11988888888', '98765432101', 'CLIENTE', 'ATIVO', 'Rua das Flores, 123', NOW(), NOW());

-- Inserir veículo
INSERT INTO veiculos (placa, marca, modelo, ano, cor, quilometragem, cliente_id, criado_em, atualizado_em)
VALUES ('ABC1234', 'Toyota', 'Corolla', 2020, 'Prata', 15000, 1, NOW(), NOW());

-- Inserir itens
INSERT INTO item (nome_item, parte_do_veiculo, imagem_ilustrativa, criado_em, atualizado_em) VALUES
('Óleo do motor', 'MOTOR', 'oleo_motor.jpg', NOW(), NOW()),
('Filtro de ar', 'MOTOR', 'filtro_ar.jpg', NOW(), NOW()),
('Pastilhas de freio', 'FREIO', 'pastilhas_freio.jpg', NOW(), NOW()),
('Fluido de freio', 'FREIO', 'fluido_freio.jpg', NOW(), NOW()),
('Amortecedores', 'SUSPENSAO', 'amortecedores.jpg', NOW(), NOW()),
('Bateria', 'ELETRICA', 'bateria.jpg', NOW(), NOW()),
('Lâmpadas', 'ELETRICA', 'lampadas.jpg', NOW(), NOW()),
('Pneus', 'RODAS', 'pneus.jpg', NOW(), NOW());

-- Inserir checklist
INSERT INTO checklists (id_veiculo, id_mecanico, quilometragem, id_status, descricao, criado_em, atualizado_em)
VALUES (1, 1, 15000, 1, 'Revisão de rotina', NOW(), NOW());

-- Inserir itens do checklist
INSERT INTO item_checklist (id_checklist, id_item, id_status_item, criado_em, atualizado_em) VALUES
(1, 1, 1, NOW(), NOW()),
(1, 2, 1, NOW(), NOW()),
(1, 3, 1, NOW(), NOW()),
(1, 4, 1, NOW(), NOW()),
(1, 5, 1, NOW(), NOW()),
(1, 6, 1, NOW(), NOW()),
(1, 7, 1, NOW(), NOW()),
(1, 8, 1, NOW(), NOW());

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