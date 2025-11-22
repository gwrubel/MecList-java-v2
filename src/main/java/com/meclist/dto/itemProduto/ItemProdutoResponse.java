package com.meclist.dto.itemProduto;


public record ItemProdutoResponse(
    Long id,
    Long idItem,
    Long idProduto,
    String nomeProduto
) {
    
}
