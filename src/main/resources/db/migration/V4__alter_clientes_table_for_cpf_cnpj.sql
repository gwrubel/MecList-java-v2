-- Migração para suportar CPF e CNPJ na tabela clientes

-- Adicionar coluna tipo_documento
ALTER TABLE clientes ADD COLUMN tipo_documento VARCHAR(10);

-- Atualizar registros existentes para CPF (assumindo que todos os registros existentes são CPFs)
UPDATE clientes SET tipo_documento = 'CPF' WHERE tipo_documento IS NULL;

-- Tornar a coluna tipo_documento NOT NULL
ALTER TABLE clientes ALTER COLUMN tipo_documento SET NOT NULL;

-- Renomear coluna cpf para documento
ALTER TABLE clientes RENAME COLUMN cpf TO documento;

-- Adicionar constraint para validar o tipo de documento
ALTER TABLE clientes ADD CONSTRAINT check_tipo_documento CHECK (tipo_documento IN ('CPF', 'CNPJ'));

-- Adicionar constraint para validar o tamanho do documento baseado no tipo
ALTER TABLE clientes ADD CONSTRAINT check_documento_length 
CHECK (
    (tipo_documento = 'CPF' AND LENGTH(documento) = 11) OR
    (tipo_documento = 'CNPJ' AND LENGTH(documento) = 14)
);





