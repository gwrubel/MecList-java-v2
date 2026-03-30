package com.meclist.dto.checklist.precificacao;

import java.math.BigDecimal;

public record ProdutoPrecificadoResponse(
    Long checklistProdutoId,
    Long produtoId,
    String nomeProduto,
    Integer quantidade,
    BigDecimal valorUnitario,
    String marca
) {}
