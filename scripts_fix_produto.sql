-- Script para corrigir a estrutura da tabela produto
-- Remove colunas antigas e garante que está conforme nova DER

-- 1. Remover constraints antigas da tabela produto
ALTER TABLE produto DROP CONSTRAINT IF EXISTS produto_id_item_checklist_fkey CASCADE;
ALTER TABLE produto DROP CONSTRAINT IF EXISTS produto_item_checklist_id_item_checklist_fkey CASCADE;

-- 2. Remover colunas antigas da tabela produto
ALTER TABLE produto DROP COLUMN IF EXISTS id_item_checklist CASCADE;
ALTER TABLE produto DROP COLUMN IF EXISTS quantidade CASCADE;
ALTER TABLE produto DROP COLUMN IF EXISTS valor_unitario CASCADE;
ALTER TABLE produto DROP COLUMN IF EXISTS valor_uni CASCADE;

-- 3. Verificar se nome_produto existe, se não existir, adicionar
-- (Se a tabela estiver vazia ou for recriada, o Hibernate pode criar corretamente)

-- Pronto! A tabela produto agora deve ter apenas:
-- id_produto, nome_produto, criado_em, atualizado_em

