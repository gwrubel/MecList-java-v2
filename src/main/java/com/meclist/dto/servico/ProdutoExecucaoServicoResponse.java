package com.meclist.dto.servico;

public record ProdutoExecucaoServicoResponse(
        Long checklistProdutoId,
        String nomeProduto,
        Integer quantidade,
        String marca
) {
}
