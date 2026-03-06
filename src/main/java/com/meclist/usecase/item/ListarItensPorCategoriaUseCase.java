package com.meclist.usecase.item;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.meclist.domain.Item;
import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.Situacao;
import com.meclist.dto.item.ItemResponse;
import com.meclist.interfaces.ItemGateway;

@Service
public class ListarItensPorCategoriaUseCase {

    private final ItemGateway itemGateway;

    public ListarItensPorCategoriaUseCase(ItemGateway itemGateway) {
        this.itemGateway = itemGateway;
    }

    public List<ItemResponse> executar(CategoriaParteVeiculo categoria, Situacao situacao) {
           List<Item> itens = itemGateway.buscarPorCategoria(categoria);

        if (situacao != null) {
            itens = itens.stream()
                    .filter(item -> situacao.equals(item.getSituacao()))
                    .collect(Collectors.toList());
        }
        
        return itens.stream()
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
