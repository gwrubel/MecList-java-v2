package com.meclist.dto.itemProduto;

import com.meclist.domain.ItemProduto;
import com.meclist.domain.enums.Situacao;

public record ProdutosDoItemResponse(
    Long id,
    Long produtoId,
    String nomeProduto,
    Situacao situacao
) {

    public static ProdutosDoItemResponse from(ItemProduto itemProduto) {
        return new ProdutosDoItemResponse(
            itemProduto.getId(),
            itemProduto.getProduto().getId(),
            itemProduto.getProduto().getNomeProduto(),
            itemProduto.getProduto().getSituacao()
        );
    }
}
