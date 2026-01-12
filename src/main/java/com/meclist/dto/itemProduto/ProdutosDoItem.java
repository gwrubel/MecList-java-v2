package com.meclist.dto.itemProduto;

import com.meclist.domain.ItemProduto;

public record ProdutosDoItem(
    Long id,
    Long idProduto,
    String nomeProduto
) {

    public static ProdutosDoItem from(ItemProduto itemProduto) {
        return new ProdutosDoItem(
            itemProduto.getId(),
            itemProduto.getProduto().getId(),
            itemProduto.getProduto().getNomeProduto()
        );
    }
}
