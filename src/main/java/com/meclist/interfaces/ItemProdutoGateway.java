package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;

import com.meclist.domain.ItemProduto;

public interface ItemProdutoGateway {
    ItemProduto salvar(ItemProduto itemProduto);
    Optional<ItemProduto> buscarPorItemEProduto(Long idItem, Long idProduto);
    List<ItemProduto> buscarPorItem(Long idItem);
    List<ItemProduto> buscarPorProduto(Long idProduto);
    boolean existeRelacionamento(Long idItem, Long idProduto);
    void excluir(Long id);
    void excluirPorItemEProduto(Long idItem, Long idProduto);
}

