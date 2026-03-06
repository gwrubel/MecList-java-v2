package com.meclist.usecase.item;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.meclist.domain.Item;
import com.meclist.domain.enums.Situacao;
import com.meclist.dto.item.ItemResponse;
import com.meclist.interfaces.ItemGateway;


@Service
public class ListarTodosItensUseCase {
      private final ItemGateway itemGateway;

    public ListarTodosItensUseCase(ItemGateway itemGateway) {
        this.itemGateway = itemGateway;
    }


    public List<ItemResponse> executar(Situacao situacao) {
        List<Item> itens = itemGateway.buscarTodos();
        
        return itens.stream()
                .filter(item -> situacao.equals(item.getSituacao()))
                .map(item -> new ItemResponse(
                        item.getId(),
                        item.getNome(),
                        item.getParteDoVeiculo(),
                        item.getImagemIlustrativa(),
                        item.getSituacao(),
                        item.getProdutos().size(),
                        item.getCriadoEm(),
                        item.getAtualizadoEm()
                ))
                .collect(Collectors.toList());
    }
}
