package com.meclist.usecase.itemProduto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Produto;
import com.meclist.dto.produto.ProdutoRequest;
import com.meclist.exception.ProdutoJaExisteException;
import com.meclist.interfaces.ProdutoGateway;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AtualizarNomeDoProdutoUseCase {
    
    private final ProdutoGateway produtoGateway;

    public AtualizarNomeDoProdutoUseCase(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

     @Transactional
    public Produto executar(Long idProduto, ProdutoRequest request) {
        var produto = produtoGateway.buscarPorId(idProduto)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
        
        // Verifica se já existe outro produto com esse nome
        produtoGateway.buscarPorNome(request.nomeProduto())
            .ifPresent(p -> {
                if (!p.getId().equals(idProduto)) {
                    throw new ProdutoJaExisteException("Já existe um produto com este nome");
                }
            });
        
        produto.atualizarNome(request.nomeProduto());
        return produtoGateway.atualizar(produto);
    }
}
