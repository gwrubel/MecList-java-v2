package com.meclist.usecase.itemProduto;

import org.springframework.stereotype.Service;

import com.meclist.interfaces.ItemProdutoGateway;
import com.meclist.domain.ItemProduto;
import com.meclist.dto.itemProduto.ItemProdutoResponse;
import java.util.List;

@Service
public class ListarProdutosPorItemUseCase {

    private final ItemProdutoGateway itemProdutoGateway;

    public ListarProdutosPorItemUseCase(ItemProdutoGateway itemProdutoGateway) {
        this.itemProdutoGateway = itemProdutoGateway;
    }

    public List<ItemProdutoResponse> executar(Long idItem) {
        List<ItemProduto> itemProdutos = itemProdutoGateway.buscarPorItem(idItem);
        
        return itemProdutos.stream()
            .map(itemProduto -> new ItemProdutoResponse(
                itemProduto.getId(),
                itemProduto.getItem().getId(),
                itemProduto.getProduto().getId(),
                itemProduto.getProduto().getNomeProduto()
            ))
            .toList();
    }
    
}
