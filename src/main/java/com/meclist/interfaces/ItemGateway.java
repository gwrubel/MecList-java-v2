package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;

import com.meclist.domain.Item;
import com.meclist.domain.enums.CategoriaParteVeiculo;

public interface ItemGateway {
    Item salvar(Item item);
    List<Item> buscarPorCategoria(CategoriaParteVeiculo categoria);
    Optional<Item> buscarPorId(Long id);
    List<Item> buscarTodos();
    boolean existeComMesmoNome(String nome);
    void excluir(Long id);
}
