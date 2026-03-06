package com.meclist.dto.produto;

public record ProdutoAdicionado(
    Long produtoId,
    String nomeProduto,
    Integer quantidade
) {
}
