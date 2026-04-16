package com.meclist.dto.checklist.aprovacao;

import java.math.BigDecimal;

public record ProdutoAprovacaoResponse(
    Long checklistProdutoId,
    String nomeProduto,
    Integer quantidade,
    BigDecimal valorUnitario,
    String marca,
    Boolean aprovadoCliente
) {}
