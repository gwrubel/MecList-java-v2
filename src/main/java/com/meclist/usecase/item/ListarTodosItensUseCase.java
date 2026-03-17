package com.meclist.usecase.item;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.meclist.domain.Item;
import com.meclist.domain.enums.Situacao;
import com.meclist.dto.item.ItemResponse;
import com.meclist.interfaces.ItemGateway;
import com.meclist.interfaces.ItemProdutoGateway;


@Service
public class ListarTodosItensUseCase {
      private final ItemGateway itemGateway;
      private final ItemProdutoGateway itemProdutoGateway;

    public ListarTodosItensUseCase(ItemGateway itemGateway, ItemProdutoGateway itemProdutoGateway) {
        this.itemGateway = itemGateway;
        this.itemProdutoGateway = itemProdutoGateway;
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
                        (int) itemProdutoGateway.contarPorItem(item.getId()),
                        item.getCriadoEm(),
                        item.getAtualizadoEm()
                ))
                .collect(Collectors.toList());
    }
}
