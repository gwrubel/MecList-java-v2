package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;

import com.meclist.domain.Produto;

public interface ProdutoGateway {
    Produto salvar(Produto produto);
    List<Produto> buscarTodos();
    Optional<Produto> buscarPorId(Long id);
    Optional<Produto> buscarPorNome(String nomeProduto);
    List<Produto> buscarPorNomeContendo(String nomeProduto);
    void excluir(Long id);
    Produto atualizar(Produto produto);
}