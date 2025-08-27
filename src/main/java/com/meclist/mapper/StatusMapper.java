package com.meclist.mapper;

import com.meclist.domain.Status;
import com.meclist.persistence.entity.StatusEntity;

public class StatusMapper {
    
    public static StatusEntity toEntity(Status status) {
        var entity = new StatusEntity();
        entity.setId(status.getId());
        entity.setDescricao(status.getDescricao());
        entity.setCriadoEm(status.getCriadoEm());
        entity.setAtualizadoEm(status.getAtualizadoEm());
        return entity;
    }
    
    public static Status toDomain(StatusEntity entity) {
        return new Status(
                entity.getId(),
                entity.getDescricao(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
}
