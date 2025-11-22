package com.meclist.mapper;

import com.meclist.domain.Produto;
import com.meclist.persistence.entity.ProdutoEntity;

public class ProdutoMapper {

    public static ProdutoEntity toEntity(Produto produto) {
        if (produto == null) {
            return null;
        }

        ProdutoEntity entity = new ProdutoEntity();
        entity.setId(produto.getId());
        entity.setNomeProduto(produto.getNomeProduto());
        entity.setCriadoEm(produto.getCriadoEm());
        entity.setAtualizadoEm(produto.getAtualizadoEm());
        
        return entity;
    }

    public static Produto toDomain(ProdutoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Produto(
                entity.getId(),
                entity.getNomeProduto(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
}