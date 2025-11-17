package com.meclist.usecase.item;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.meclist.domain.Item;
import com.meclist.dto.item.ItemResponse;
import com.meclist.interfaces.ItemGateway;

@Service
public class ListarTodosItensUseCase {

    private final ItemGateway itemGateway;

    public ListarTodosItensUseCase(ItemGateway itemGateway) {
        this.itemGateway = itemGateway;
    }

    public List<ItemResponse> executar() {
        List<Item> itens = itemGateway.buscarTodos();
        
        return itens.stream()
                .map(item -> new ItemResponse(
                        item.getId(),
                        item.getNome(),
                        item.getParteDoVeiculo(),
                        item.getImagemIlustrativa(),
                        item.getCriadoEm(),
                        item.getAtualizadoEm()
                ))
                .collect(Collectors.toList());
    }
}