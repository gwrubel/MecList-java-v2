package com.meclist.mapper;

import com.meclist.domain.StatusItem;
import com.meclist.persistence.entity.StatusItemEntity;

public class StatusItemMapper {

    public static StatusItemEntity toEntity(StatusItem statusItem) {
        if (statusItem == null) {
            return null;
        }

        var entity = new StatusItemEntity();
        entity.setId(statusItem.getId());
        entity.setDescricao(statusItem.getDescricao());
        
        return entity;
    }

    public static StatusItem toDomain(StatusItemEntity entity) {
        if (entity == null) {
            return null;
        }

        return new StatusItem(
                entity.getId(),
                entity.getDescricao()
        );
    }
}