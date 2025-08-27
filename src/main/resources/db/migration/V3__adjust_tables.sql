-- Remover tabelas que não existem no modelo proposto
DROP TABLE IF EXISTS pagamento;
DROP TABLE IF EXISTS item_orcamento;

-- Adicionar tabela de produtos
CREATE TABLE IF NOT EXISTS produto (
    id_produto SERIAL PRIMARY KEY,
    id_item_checklist INTEGER NOT NULL,
    nome_produto VARCHAR(255) NOT NULL,
    quantidade INTEGER NOT NULL,
    valor_unitario FLOAT NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    FOREIGN KEY (id_item_checklist) REFERENCES item_checklist(id_item_checklist)
);

-- Alterar a tabela de orcamento para se relacionar com checklist em vez de servico
ALTER TABLE orcamento DROP CONSTRAINT orcamento_id_servico_fkey;
ALTER TABLE orcamento RENAME COLUMN id_servico TO id_checklist;
ALTER TABLE orcamento ADD CONSTRAINT orcamento_id_checklist_fkey FOREIGN KEY (id_checklist) REFERENCES checklists(id_checklist);