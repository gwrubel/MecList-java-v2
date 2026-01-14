package com.meclist.dto.itemProduto;

import com.meclist.domain.ItemProduto;

public record ProdutosDoItemResponse(
    Long id,
    Long idProduto,
    String nomeProduto
) {

    public static ProdutosDoItemResponse from(ItemProduto itemProduto) {
        return new ProdutosDoItemResponse(
            itemProduto.getId(),
            itemProduto.getProduto().getId(),
            itemProduto.getProduto().getNomeProduto()
        );
    }
}
