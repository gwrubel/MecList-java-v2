package com.meclist.usecase.itemProduto;

import org.springframework.stereotype.Service;

import com.meclist.interfaces.ItemGateway;
import com.meclist.interfaces.ItemProdutoGateway;

import jakarta.persistence.EntityNotFoundException;

import com.meclist.dto.itemProduto.ProdutosDoItem;
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

public List<ProdutosDoItem> executar(Long idItem) {
    itemGateway.buscarPorId(idItem)
        .orElseThrow(() -> new EntityNotFoundException(
            "Item com ID " + idItem + " não encontrado"
        ));
    
    return itemProdutoGateway.buscarPorItem(idItem)
        .stream()
        .map(ProdutosDoItem::from) 
        .toList();
}
}
