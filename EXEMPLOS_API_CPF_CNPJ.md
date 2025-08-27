# Exemplos de Uso da API - Suporte a CPF e CNPJ

## Cadastro de Cliente com CPF

### Request
```http
POST /clientes
Content-Type: application/json

{
    "nome": "João Silva",
    "email": "joao.silva@email.com",
    "senha": "senha123",
    "telefone": "11999887766",
    "documento": "12345678901",
    "tipoDocumento": "CPF",
    "endereco": "Rua das Flores, 123 - São Paulo/SP"
}
```

### Response
```http
HTTP/1.1 201 Created
Content-Type: application/json

{
    "timestamp": "2025-08-27T13:50:00",
    "status": 201,
    "message": "Cliente cadastrado com sucesso!",
    "path": "/clientes",
    "data": null
}
```

## Cadastro de Cliente com CNPJ

### Request
```http
POST /clientes
Content-Type: application/json

{
    "nome": "Empresa ABC Ltda",
    "email": "contato@empresaabc.com",
    "senha": "senha123",
    "telefone": "1133221144",
    "documento": "12345678000195",
    "tipoDocumento": "CNPJ",
    "endereco": "Av. Paulista, 1000 - São Paulo/SP"
}
```

### Response
```http
HTTP/1.1 201 Created
Content-Type: application/json

{
    "timestamp": "2025-08-27T13:50:00",
    "status": 201,
    "message": "Cliente cadastrado com sucesso!",
    "path": "/clientes",
    "data": null
}
```

## Buscar Cliente por ID

### Request
```http
GET /clientes/1
```

### Response (Cliente com CPF)
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1,
    "nome": "João Silva",
    "email": "joao.silva@email.com",
    "documento": "12345678901",
    "tipoDocumento": "CPF",
    "telefone": "11999887766",
    "endereco": "Rua das Flores, 123 - São Paulo/SP",
    "tipoDeUsuario": "CLIENTE",
    "situacao": "ATIVO",
    "criadoEm": "2025-08-27T13:50:00",
    "atualizadoEm": "2025-08-27T13:50:00",
    "veiculos": [
        {
            "id": 1,
            "placa": "ABC1234",
            "modelo": "Gol",
            "marca": "Volkswagen",
            "cor": "Branco",
            "ano": 2020,
            "quilometragem": 45000.0,
            "dataUltimaRevisao": "2024-12-15"
        },
        {
            "id": 2,
            "placa": "XYZ5678",
            "modelo": "Onix",
            "marca": "Chevrolet",
            "cor": "Prata",
            "ano": 2021,
            "quilometragem": 32000.0,
            "dataUltimaRevisao": "2024-10-20"
        }
    ]
}
```

### Response (Cliente com CNPJ)
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 2,
    "nome": "Empresa ABC Ltda",
    "email": "contato@empresaabc.com",
    "documento": "12345678000195",
    "tipoDocumento": "CNPJ",
    "telefone": "1133221144",
    "endereco": "Av. Paulista, 1000 - São Paulo/SP",
    "tipoDeUsuario": "CLIENTE",
    "situacao": "ATIVO",
    "criadoEm": "2025-08-27T13:50:00",
    "atualizadoEm": "2025-08-27T13:50:00",
    "veiculos": [
        {
            "id": 3,
            "placa": "DEF9012",
            "modelo": "Sprinter",
            "marca": "Mercedes-Benz",
            "cor": "Azul",
            "ano": 2019,
            "quilometragem": 85000.0,
            "dataUltimaRevisao": "2024-11-10"
        }
    ]
}
```

## Atualizar Dados do Cliente

### Request (Atualizar CPF)
```http
PUT /clientes/1
Content-Type: application/json

{
    "nome": "João Silva Santos",
    "telefone": "11988776655",
    "documento": "98765432100",
    "tipoDocumento": "CPF",
    "endereco": "Rua das Palmeiras, 456 - São Paulo/SP"
}
```

### Request (Atualizar CNPJ)
```http
PUT /clientes/2
Content-Type: application/json

{
    "nome": "Empresa ABC Comércio Ltda",
    "telefone": "1144332211",
    "documento": "98765432000187",
    "tipoDocumento": "CNPJ",
    "endereco": "Av. Brigadeiro Faria Lima, 2000 - São Paulo/SP"
}
```

### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "timestamp": "2025-08-27T13:55:00",
    "status": 200,
    "message": "Cliente atualizado com sucesso!",
    "path": "/clientes/1",
    "data": null
}
```

## Listar Todos os Clientes

### Request
```http
GET /clientes
```

### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "nome": "João Silva Santos",
        "email": "joao.silva@email.com",
        "documento": "98765432100",
        "tipoDocumento": "CPF",
        "telefone": "11988776655",
        "endereco": "Rua das Palmeiras, 456 - São Paulo/SP",
        "tipoDeUsuario": "CLIENTE",
        "situacao": "ATIVO",
        "criadoEm": "2025-08-27T13:50:00",
        "atualizadoEm": "2025-08-27T13:55:00",
        "veiculos": [
            {
                "id": 1,
                "placa": "ABC1234",
                "modelo": "Gol",
                "marca": "Volkswagen",
                "cor": "Branco",
                "ano": 2020,
                "quilometragem": 45000.0,
                "dataUltimaRevisao": "2024-12-15"
            }
        ]
    },
    {
        "id": 2,
        "nome": "Empresa ABC Comércio Ltda",
        "email": "contato@empresaabc.com",
        "documento": "98765432000187",
        "tipoDocumento": "CNPJ",
        "telefone": "1144332211",
        "endereco": "Av. Brigadeiro Faria Lima, 2000 - São Paulo/SP",
        "tipoDeUsuario": "CLIENTE",
        "situacao": "ATIVO",
        "criadoEm": "2025-08-27T13:50:00",
        "atualizadoEm": "2025-08-27T13:55:00",
        "veiculos": [
            {
                "id": 3,
                "placa": "DEF9012",
                "modelo": "Sprinter",
                "marca": "Mercedes-Benz",
                "cor": "Azul",
                "ano": 2019,
                "quilometragem": 85000.0,
                "dataUltimaRevisao": "2024-11-10"
            }
        ]
    }
]
```

## Listar Clientes por Situação

### Request
```http
GET /clientes?situacao=ATIVO
```

### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": 1,
        "nome": "João Silva Santos",
        "email": "joao.silva@email.com",
        "documento": "98765432100",
        "tipoDocumento": "CPF",
        "telefone": "11988776655",
        "endereco": "Rua das Palmeiras, 456 - São Paulo/SP",
        "tipoDeUsuario": "CLIENTE",
        "situacao": "ATIVO",
        "criadoEm": "2025-08-27T13:50:00",
        "atualizadoEm": "2025-08-27T13:55:00",
        "veiculos": [
            {
                "id": 1,
                "placa": "ABC1234",
                "modelo": "Gol",
                "marca": "Volkswagen",
                "cor": "Branco",
                "ano": 2020,
                "quilometragem": 45000.0,
                "dataUltimaRevisao": "2024-12-15"
            }
        ]
    }
]
```

## Validações e Erros

### Erro: Documento Inválido (CPF)
```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
    "timestamp": "2025-08-27T13:50:00",
    "status": 400,
    "error": "ValidationError",
    "message": "CPF inválido!",
    "path": "/clientes"
}
```

### Erro: Documento Inválido (CNPJ)
```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
    "timestamp": "2025-08-27T13:50:00",
    "status": 400,
    "error": "ValidationError",
    "message": "CNPJ inválido!",
    "path": "/clientes"
}
```

### Erro: Documento Já Cadastrado
```http
HTTP/1.1 409 Conflict
Content-Type: application/json

{
    "timestamp": "2025-08-27T13:50:00",
    "status": 409,
    "error": "Duplicidade",
    "message": "Documento já cadastrado!",
    "path": "/clientes"
}
```

### Erro: Tipo de Documento Obrigatório
```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
    "timestamp": "2025-08-27T13:50:00",
    "status": 400,
    "error": "ValidationError",
    "message": "Campos inválidos.",
    "errors": {
        "tipoDocumento": "O tipo de documento é obrigatório"
    },
    "path": "/clientes"
}
```

## Códigos de Status HTTP

- **200 OK**: Operação realizada com sucesso
- **201 Created**: Cliente criado com sucesso
- **400 Bad Request**: Dados inválidos ou campos obrigatórios ausentes
- **404 Not Found**: Cliente não encontrado
- **409 Conflict**: Documento ou email já cadastrado

## Observações Importantes

1. **Tipo de Documento**: Sempre deve ser especificado como "CPF" ou "CNPJ"
2. **Validação**: O sistema valida automaticamente o formato baseado no tipo
3. **Unicidade**: Documento e email devem ser únicos no sistema
4. **Migração**: Registros existentes são automaticamente convertidos para CPF
5. **Compatibilidade**: A API mantém compatibilidade com validações existentes
6. **Veículos**: A lista de veículos é sempre incluída no response para facilitar a visualização da frota do cliente
