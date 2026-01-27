package com.meclist.usecase.item;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.meclist.domain.Item;
import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.dto.item.ItemResponse;
import com.meclist.dto.item.ItemsPorCategoriaResponse;
import com.meclist.interfaces.ItemGateway;

@Service
public class ListarItensAgrupadosUseCase {

    private final ItemGateway itemGateway;

    public ListarItensAgrupadosUseCase(ItemGateway itemGateway) {
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
                                    item.getImagemIlustrativa(),
                                    item.getProdutos().size(),
                                    item.getCriadoEm(),
                                    item.getAtualizadoEm()
                                    
                            ))
                            .collect(Collectors.toList());
                    
                    return new ItemsPorCategoriaResponse(categoria, itemResponses);
                })
                .collect(Collectors.toList());
    }
}