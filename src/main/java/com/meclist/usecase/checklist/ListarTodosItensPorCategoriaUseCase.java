package com.meclist.usecase.checklist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.meclist.domain.Item;
import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.dto.checklist.ItemResponse;
import com.meclist.dto.checklist.ItemsPorCategoriaResponse;
import com.meclist.interfaces.ItemGateway;

@Service
public class ListarTodosItensPorCategoriaUseCase {

    private final ItemGateway itemGateway;

    public ListarTodosItensPorCategoriaUseCase(ItemGateway itemGateway) {
        this.itemGateway = itemGateway;
    }

    public List<ItemsPorCategoriaResponse> executar() {
        // Busca todos os itens de uma vez
        List<Item> todosItens = itemGateway.buscarTodos();
        
        // Agrupa os itens por categoria
        Map<CategoriaParteVeiculo, List<Item>> itensPorCategoria = todosItens.stream()
                .collect(Collectors.groupingBy(Item::getParteDoVeiculo));
        
        // Converte para o formato de resposta
        return itensPorCategoria.entrySet().stream()
                .map(entry -> {
                    CategoriaParteVeiculo categoria = entry.getKey();
                    List<ItemResponse> itemResponses = entry.getValue().stream()
                            .map(item -> new ItemResponse(
                                    item.getId(),
                                    item.getNome(),
                                    item.getParteDoVeiculo(),
                                    item.getImagemIlustrativa()
                            ))
                            .collect(Collectors.toList());
                    
                    return new ItemsPorCategoriaResponse(categoria, itemResponses);
                })
                .collect(Collectors.toList());
    }
}