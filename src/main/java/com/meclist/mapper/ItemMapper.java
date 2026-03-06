package com.meclist.mapper;

import java.util.List;


import com.meclist.domain.Item;
import com.meclist.domain.Produto;
import com.meclist.dto.item.ItemResponse;
import com.meclist.persistence.entity.ItemEntity;


public class ItemMapper {

    public static ItemEntity toEntity(Item item) {
        if (item == null) {
            return null;
        }

        ItemEntity entity = new ItemEntity();
        entity.setId(item.getId());
        entity.setNomeItem(item.getNome());
        entity.setParteDoVeiculo(item.getParteDoVeiculo());
        entity.setImagemIlustrativa(item.getImagemIlustrativa());
        entity.setSituacao(item.getSituacao());
        entity.setCriadoEm(item.getCriadoEm());
        entity.setAtualizadoEm(item.getAtualizadoEm());
        
        return entity;
    }

    public static Item toDomain(ItemEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Item(
            entity.getId(),
            entity.getNomeItem(),
            entity.getParteDoVeiculo(),
            entity.getImagemIlustrativa(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm(),
            entity.getSituacao()         
        );
    }

    public static Item ItemComProdutosToDomain(ItemEntity entity, List<Produto> produtos) {
        if (entity == null) {
            return null;
        }

        Item item = new Item(
            entity.getId(),
            entity.getNomeItem(),
            entity.getParteDoVeiculo(),
            entity.getImagemIlustrativa(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm(),
            entity.getSituacao()
        );

        
        
        // Adicionar produtos diretamente
        if (produtos != null) {
            produtos.forEach(item::adicionarProduto);
        }
        
        return item;
    }


    public static ItemResponse toResponse(Item item) {
        return new ItemResponse(item.getId(), item.getNome(), item.getParteDoVeiculo(), item.getImagemIlustrativa(), item.getSituacao(), item.getProdutos().size(), item.getCriadoEm(), item.getAtualizadoEm());
    }

    // Método para copiar os dados de um Item para um ItemEntity existente (útil para atualizações)
    public static void copyToEntity(Item item, ItemEntity entity) {
        entity.setNomeItem(item.getNome());
        entity.setParteDoVeiculo(item.getParteDoVeiculo());
        entity.setImagemIlustrativa(item.getImagemIlustrativa());
        entity.setSituacao(item.getSituacao());
        entity.setAtualizadoEm(item.getAtualizadoEm());
        if (entity.getCriadoEm() == null) {
            entity.setCriadoEm(item.getCriadoEm());
        }
    }

}
