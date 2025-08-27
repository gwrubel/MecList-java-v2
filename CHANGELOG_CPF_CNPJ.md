# Changelog: Suporte a CPF e CNPJ para Clientes

## Resumo das Mudanças

Este documento descreve as alterações implementadas para permitir que clientes sejam cadastrados tanto com CPF quanto com CNPJ, ao invés de apenas CPF.

## Arquivos Alterados

### 1. Novo Enum: TipoDocumento
- **Arquivo**: `src/main/java/com/meclist/domain/enums/TipoDocumento.java`
- **Mudança**: Criado enum com valores `CPF` e `CNPJ`

### 2. Domain: Cliente
- **Arquivo**: `src/main/java/com/meclist/domain/Cliente.java`
- **Mudanças**:
  - Campo `cpf` alterado para `documento`
  - Adicionado campo `tipoDocumento` do tipo `TipoDocumento`
  - Construtores atualizados
  - Método `atualizarCpf()` alterado para `atualizarDocumento()`
  - Método `novoCadastro()` atualizado

### 3. Entity: ClienteEntity
- **Arquivo**: `src/main/java/com/meclist/persistence/entity/ClienteEntity.java`
- **Mudanças**:
  - Campo `cpf` alterado para `documento`
  - Adicionado campo `tipoDocumento` com anotação `@Enumerated(EnumType.STRING)`

### 4. DTOs
#### ClienteRequest
- **Arquivo**: `src/main/java/com/meclist/dto/cliente/ClienteRequest.java`
- **Mudanças**:
  - Campo `cpf` alterado para `documento`
  - Adicionado campo `tipoDocumento` com validação `@NotNull`
  - Removida validação de regex específica para CPF

#### ClienteResponse
- **Arquivo**: `src/main/java/com/meclist/dto/cliente/ClienteResponse.java`
- **Mudanças**:
  - Convertido para record
  - Campo `cpf` alterado para `documento`
  - Adicionado campo `tipoDocumento`
  - **Mantida lista de veículos** para facilitar visualização da frota

#### AtualizarClienteRequest
- **Arquivo**: `src/main/java/com/meclist/dto/cliente/AtualizarClienteRequest.java`
- **Mudanças**:
  - Campo `cpf` alterado para `documento`
  - Adicionado campo `tipoDocumento`

### 5. Mapper: ClienteMapper
- **Arquivo**: `src/main/java/com/meclist/mapper/ClienteMapper.java`
- **Mudanças**:
  - Método `toEntity()`: mapeia `documento` e `tipoDocumento`
  - Método `toDomain()`: reconstrói cliente com novos campos
  - Método `toResponse()`: mapeia veículos e inclui no response

### 6. Interface: ClienteGateway
- **Arquivo**: `src/main/java/com/meclist/interfaces/ClienteGateway.java`
- **Mudanças**:
  - Método `buscarPorCpf()` alterado para `buscarPorDocumento()`

### 7. Implementação: ClienteJpaGatewayImpl
- **Arquivo**: `src/main/java/com/meclist/persistence/gateway/ClienteJpaGatewayImpl.java`
- **Mudanças**:
  - Método `buscarPorCpf()` alterado para `buscarPorDocumento()`
  - Verificações de duplicidade atualizadas para usar `documento`

### 8. Repository: ClienteRepository
- **Arquivo**: `src/main/java/com/meclist/persistence/repository/ClienteRepository.java`
- **Mudanças**:
  - Método `findByCpf()` alterado para `findByDocumento()`

### 9. Use Cases
#### CadastrarClienteUseCase
- **Arquivo**: `src/main/java/com/meclist/usecase/cliente/CadastrarClienteUseCase.java`
- **Mudanças**:
  - Validação específica para CPF ou CNPJ baseada no `tipoDocumento`
  - Verificação de duplicidade usando `documento`
  - Criação do cliente com novos campos

#### AtualizarDadosClienteUseCase
- **Arquivo**: `src/main/java/com/meclist/usecase/cliente/AtualizarDadosClienteUseCase.java`
- **Mudanças**:
  - Validação específica para CPF ou CNPJ na atualização
  - Uso do método `atualizarDocumento()`

### 10. Migração de Banco de Dados
- **Arquivo**: `src/main/resources/db/migration/V4__alter_clientes_table_for_cpf_cnpj.sql`
- **Mudanças**:
  - Adiciona coluna `tipo_documento`
  - Renomeia coluna `cpf` para `documento`
  - Adiciona constraints de validação
  - Atualiza registros existentes para CPF

## Validações Implementadas

### Validação de CPF
- Deve ter exatamente 11 dígitos
- Validação de dígitos verificadores
- Não pode ter todos os dígitos iguais

### Validação de CNPJ
- Deve ter exatamente 14 dígitos
- Validação de dígitos verificadores
- Não pode ter todos os dígitos iguais

### Validação de Documento
- Baseada no tipo selecionado (CPF ou CNPJ)
- Validação automática no cadastro e atualização

## Impacto nas APIs

### Endpoint: POST /clientes
- **Request**: Agora requer `documento` e `tipoDocumento` ao invés de `cpf`
- **Validação**: Validação específica baseada no tipo de documento

### Endpoint: PUT /clientes/{id}
- **Request**: Suporta atualização de `documento` e `tipoDocumento`
- **Validação**: Validação específica na atualização

### Endpoint: GET /clientes/{id}
- **Response**: Retorna `documento`, `tipoDocumento` e **lista de veículos**

## Compatibilidade

- **Registros existentes**: Serão migrados automaticamente para CPF
- **APIs**: Requerem atualização do frontend para enviar novos campos
- **Validações**: Mais rigorosas e específicas por tipo de documento
- **Veículos**: Lista de veículos mantida para facilitar visualização da frota

## Próximos Passos

1. **Testes**: Implementar testes unitários e de integração
2. **Frontend**: Atualizar formulários para suportar seleção de tipo de documento
3. **Documentação**: Atualizar documentação da API
4. **Validação**: Testar migração em ambiente de desenvolvimento

## Observações

- O sistema de Mecânicos ainda usa apenas CPF (não foi alterado)
- A migração assume que todos os registros existentes são CPFs
- As constraints de banco garantem integridade dos dados
- **A lista de veículos é mantida no response para facilitar a visualização da frota do cliente**
