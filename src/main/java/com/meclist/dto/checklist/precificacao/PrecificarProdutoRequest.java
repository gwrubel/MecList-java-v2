package com.meclist.dto.checklist.precificacao;

import java.math.BigDecimal;

public record PrecificarProdutoRequest(
    Long checklistProdutoId,
    BigDecimal valorUnitario,
    String marca
) {

}
