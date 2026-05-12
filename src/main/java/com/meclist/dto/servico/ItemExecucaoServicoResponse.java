package com.meclist.dto.servico;

import java.util.List;

import com.meclist.domain.enums.CategoriaParteVeiculo;

public record ItemExecucaoServicoResponse(
        Long itemChecklistId,
        String nomeDoItem,
        String imagemIlustrativa,
        CategoriaParteVeiculo parteDoVeiculo,
        List<ProdutoExecucaoServicoResponse> produtosAutorizados
) {
}
