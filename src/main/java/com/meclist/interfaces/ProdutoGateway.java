package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;

import com.meclist.domain.Produto;

public interface ProdutoGateway {
    Produto salvar(Produto produto);
    List<Produto> buscarPorItemChecklist(Long idItemChecklist);
    Optional<Produto> buscarPorId(Long id);
    void excluir(Long id);
}