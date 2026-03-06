package com.meclist.usecase.itemProduto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Produto;
import com.meclist.domain.enums.Situacao;
import com.meclist.exception.AssociacaoProdutoItemNaoEncontradaException;
import com.meclist.interfaces.ItemProdutoGateway;
import com.meclist.interfaces.ProdutoGateway;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AlterarSituacaoDoProdutoPorItemUseCase {

    private final ProdutoGateway produtoGateway;
    private final ItemProdutoGateway itemProdutoGateway;

    public AlterarSituacaoDoProdutoPorItemUseCase(ProdutoGateway produtoGateway,
                                                  ItemProdutoGateway itemProdutoGateway) {
        this.produtoGateway = produtoGateway;
        this.itemProdutoGateway = itemProdutoGateway;
    }

    @Transactional
    public void desativar(Long idProduto, Long idItem) { 
        executar(idProduto, idItem, Situacao.INATIVO);
    }

    @Transactional
    public void ativar(Long idProduto, Long idItem) {
        executar(idProduto, idItem, Situacao.ATIVO);
    }

    private void executar(Long idProduto, Long idItem, Situacao situacao) {
        // mesma parte inicial do AtualizarNomeDoProdutoUseCase
        Produto produto = produtoGateway.buscarPorId(idProduto)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

        if (!itemProdutoGateway.existeRelacionamento(idItem, idProduto)) {
            throw new AssociacaoProdutoItemNaoEncontradaException("Produto não está associado a esse item");
        }

       
        if (situacao == Situacao.ATIVO) {
            produto.ativar();
        } else if (situacao == Situacao.INATIVO) {
            produto.desativar();
        } else {
            throw new IllegalArgumentException("Situação inválida: " + situacao);
        }

        System.out.println("Produto " + produto.getNomeProduto() + " agora está " + produto.getSituacao());
        produtoGateway.atualizar(produto);
    }
}
