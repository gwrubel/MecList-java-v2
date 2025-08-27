package com.meclist.mapper;

import com.meclist.domain.Item;
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
                entity.getAtualizadoEm()
        );
    }
}
