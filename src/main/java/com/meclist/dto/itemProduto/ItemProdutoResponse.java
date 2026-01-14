package com.meclist.dto.itemProduto;

public record ItemProdutoResponse(
    Long id,
    Long idItem,
    Long idProduto,
    String nomeProduto
) {
    public ItemProdutoResponse(Long id, Long idItem, Long idProduto, String nomeProduto) {
        this.id = id;
        this.idItem = idItem;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
    }
}