package com.meclist.usecase.itemProduto;

import org.springframework.stereotype.Service;

import com.meclist.domain.enums.Situacao;
import com.meclist.interfaces.ItemGateway;
import com.meclist.interfaces.ItemProdutoGateway;


import com.meclist.dto.itemProduto.ProdutosDoItemResponse;
import com.meclist.exception.ItemNaoEncontradoException;

import java.util.List;

@Service
public class ListarProdutosPorItemUseCase {

    private final ItemProdutoGateway itemProdutoGateway;
    private final ItemGateway itemGateway; 

    public ListarProdutosPorItemUseCase(
            ItemProdutoGateway itemProdutoGateway,
            ItemGateway itemGateway) {
        this.itemProdutoGateway = itemProdutoGateway;
        this.itemGateway = itemGateway;
    }

public List<ProdutosDoItemResponse> executar(Long idItem, Situacao situacao) {
    itemGateway.buscarPorId(idItem)
        .orElseThrow(() -> new ItemNaoEncontradoException(
            "Item com ID " + idItem + " não encontrado"
        ));
    
    return itemProdutoGateway.buscarPorItem(idItem)
        .stream()
        .filter(ip -> situacao == null || situacao.equals(ip.getProduto().getSituacao()))
        .map(ProdutosDoItemResponse::from) 
        .toList();
}
}
