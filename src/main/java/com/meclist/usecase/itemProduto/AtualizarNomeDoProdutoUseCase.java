package com.meclist.usecase.itemProduto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Item;
import com.meclist.domain.ItemProduto;
import com.meclist.domain.Produto;
import com.meclist.dto.itemProduto.ItemProdutoResponse;
import com.meclist.dto.produto.ProdutoRequest;
import com.meclist.exception.AssociacaoProdutoItemNaoEncontradaException;
import com.meclist.exception.ProdutoJaExisteException;
import com.meclist.interfaces.ItemProdutoGateway;
import com.meclist.interfaces.ProdutoGateway;
import com.meclist.mapper.ItemProdutoMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AtualizarNomeDoProdutoUseCase {
    
    private final ProdutoGateway produtoGateway;
    private final ItemProdutoGateway itemProdutoGateway; 
    
    public AtualizarNomeDoProdutoUseCase(ProdutoGateway produtoGateway, ItemProdutoGateway itemProdutoGateway) {
        this.produtoGateway = produtoGateway;
        this.itemProdutoGateway = itemProdutoGateway;
    }
    
    @Transactional
    public ItemProdutoResponse executar(Long idProduto, ProdutoRequest request, Long idItem) {
        var produto = produtoGateway.buscarPorId(idProduto)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
        
        // Verificar se o produto está associado a pelo menos um item
        if (!itemProdutoGateway.existeRelacionamento(idItem, idProduto)) {
            throw new AssociacaoProdutoItemNaoEncontradaException("Produto não está associado a esse item");
        }
        
        // Verifica se já existe outro produto com esse nome
        produtoGateway.buscarPorNome(request.nomeProduto())
            .ifPresent(p -> {
                if (!p.getId().equals(idProduto)) {
                    throw new ProdutoJaExisteException("Já existe um produto com este nome");
                }
            });
        
        produto.atualizarNome(request.nomeProduto());
        Produto produtoSalvo = produtoGateway.atualizar(produto);

        ItemProduto itemProduto = itemProdutoGateway.buscarPorItemEProduto(idItem, produtoSalvo.getId())
        .orElseThrow(() -> new AssociacaoProdutoItemNaoEncontradaException("Associação não encontrada após atualização"));
        return ItemProdutoMapper.toResponse(itemProduto);
    }
}
