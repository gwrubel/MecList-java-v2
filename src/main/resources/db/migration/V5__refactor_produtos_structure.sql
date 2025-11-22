-- Migration para refatorar estrutura de produtos conforme nova DER
-- Separação entre catálogo (produto), template (item_produto) e execução (checklist_produto)
-- ATENÇÃO: Esta migration vai APAGAR todos os dados das tabelas de produtos

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
-- Relacionamento N:N entre Item e Produto para configuração do Admin
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
-- Relaciona produtos orçados com um item_checklist específico
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
