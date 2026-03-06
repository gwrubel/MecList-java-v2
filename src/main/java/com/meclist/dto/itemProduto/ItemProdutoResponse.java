package com.meclist.dto.itemProduto;

import com.meclist.domain.enums.Situacao;

public record ItemProdutoResponse(
    Long id,
    Long idItem,
    Long produtoId,
    String nomeProduto,
    Situacao situacao
) {
    public ItemProdutoResponse(Long id, Long idItem, Long produtoId, String nomeProduto, Situacao situacao) {
        this.id = id;
        this.idItem = idItem;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.situacao = situacao;
    }
}