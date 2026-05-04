package com.meclist.dto.checklist.precificacao;

import java.math.BigDecimal;

public record PrecificarProdutoRequest(
    Long checklistProdutoId, // null = produto novo
    Long produtoId,           // obrigatório quando checklistProdutoId é null
    Integer quantidade,       // obrigatório quando checklistProdutoId é null
    BigDecimal valorUnitario,
    String marca
) {

}
