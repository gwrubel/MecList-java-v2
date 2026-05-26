package com.meclist.dto.checklist.visualizacao;

import java.math.BigDecimal;

public record ProdutoVisualizacaoCompletaResponse(
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal preco,
        String marca
) {
}
