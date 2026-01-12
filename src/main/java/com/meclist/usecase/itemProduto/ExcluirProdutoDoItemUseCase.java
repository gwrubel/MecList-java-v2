package com.meclist.usecase.itemProduto;

import org.springframework.stereotype.Service;

import com.meclist.interfaces.ItemProdutoGateway;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExcluirProdutoDoItemUseCase {


       private final ItemProdutoGateway itemProdutoGateway;

    public ExcluirProdutoDoItemUseCase(ItemProdutoGateway itemProdutoGateway) {
        this.itemProdutoGateway = itemProdutoGateway;
    }
    
    @Transactional
    public void executar(Long idItem, Long idProduto) {
        // Verifica se existe relacionamento
        if (!itemProdutoGateway.existeRelacionamento(idItem, idProduto)) {
            throw new EntityNotFoundException(
                "Produto não está associado a este item"
            );
        }
        
        // Remove a associação
        itemProdutoGateway.excluirPorItemEProduto(idItem, idProduto);
    }
}
