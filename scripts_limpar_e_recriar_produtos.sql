-- Script completo para limpar e recriar estrutura de produtos conforme nova DER
-- ATENÇÃO: Este script vai APAGAR TODOS OS DADOS das tabelas relacionadas a produtos

BEGIN;

-- 1. Remover todas as tabelas relacionadas (em ordem de dependência)
DROP TABLE IF EXISTS checklist_produto CASCADE;
DROP TABLE IF EXISTS item_produto CASCADE;
DROP TABLE IF EXISTS produto CASCADE;

-- 2. Criar tabela produto (catálogo agnóstico)
CREATE TABLE produto (
    id_produto SERIAL PRIMARY KEY,
    nome_produto VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP
);

-- 3. Criar tabela item_produto (Template/Sugestão)
CREATE TABLE item_produto (
    id_item_produto SERIAL PRIMARY KEY,
    id_item INTEGER NOT NULL,
    id_produto INTEGER NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    FOREIGN KEY (id_item) REFERENCES item(id_item),
    FOREIGN KEY (id_produto) REFERENCES produto(id_produto),
    UNIQUE(id_item, id_produto)
);

-- 4. Criar tabela checklist_produto (Execução/Orçamento)
CREATE TABLE checklist_produto (
    id_checklist_produto SERIAL PRIMARY KEY,
    id_item_checklist INTEGER NOT NULL,
    id_produto INTEGER NOT NULL,
    quantidade INTEGER NOT NULL,
    valor_uni DECIMAL(10,2),
    aprovado_cliente BOOLEAN,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    FOREIGN KEY (id_item_checklist) REFERENCES item_checklist(id_item_checklist),
    FOREIGN KEY (id_produto) REFERENCES produto(id_produto)
);

-- 5. Criar índices para melhor performance
CREATE INDEX idx_item_produto_item ON item_produto(id_item);
CREATE INDEX idx_item_produto_produto ON item_produto(id_produto);
CREATE INDEX idx_checklist_produto_item_checklist ON checklist_produto(id_item_checklist);
CREATE INDEX idx_checklist_produto_produto ON checklist_produto(id_produto);
CREATE INDEX idx_checklist_produto_aprovado ON checklist_produto(aprovado_cliente);

COMMIT;

-- 6. Marcar a migration V5 como executada no Flyway (ajuste a checksum se necessário)
-- Execute este INSERT apenas se a migration não aparecer como executada
INSERT INTO flyway_schema_history 
(installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success)
VALUES 
((SELECT COALESCE(MAX(installed_rank), 0) + 1 FROM flyway_schema_history), 
 '5', 
 'refactor produtos structure', 
 'SQL', 
 'V5__refactor_produtos_structure.sql', 
 0, 
 'postgres', 
 NOW(), 
 0, 
 true)
ON CONFLICT DO NOTHING;

