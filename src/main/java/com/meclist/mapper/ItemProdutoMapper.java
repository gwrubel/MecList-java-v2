package com.meclist.mapper;

import com.meclist.domain.Item;
import com.meclist.domain.ItemProduto;
import com.meclist.domain.Produto;
import com.meclist.persistence.entity.ItemEntity;
import com.meclist.persistence.entity.ItemProdutoEntity;
import com.meclist.persistence.entity.ProdutoEntity;

public class ItemProdutoMapper {

    public static ItemProdutoEntity toEntity(ItemProduto itemProduto, ItemEntity item, ProdutoEntity produto) {
        if (itemProduto == null) {
            return null;
        }

        ItemProdutoEntity entity = new ItemProdutoEntity();
        entity.setId(itemProduto.getId());
        entity.setItem(item);
        entity.setProduto(produto);
        entity.setCriadoEm(itemProduto.getCriadoEm());
        entity.setAtualizadoEm(itemProduto.getAtualizadoEm());
        
        return entity;
    }

    public static ItemProduto toDomain(ItemProdutoEntity entity) {
        if (entity == null) {
            return null;
        }

        Item item = ItemMapper.toDomain(entity.getItem());
        Produto produto = ProdutoMapper.toDomain(entity.getProduto());

        return new ItemProduto(
                entity.getId(),
                item,
                produto,
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
}

