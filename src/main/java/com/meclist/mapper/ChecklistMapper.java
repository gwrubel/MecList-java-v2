package com.meclist.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.meclist.domain.Checklist;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.dto.checklist.ChecklistResponse;
import com.meclist.dto.itemChecklist.ItemChecklistResponse;
import com.meclist.persistence.entity.ChecklistEntity;
import com.meclist.persistence.entity.ItemChecklistEntity;

public class ChecklistMapper {

    // Domain → Entity
    public static ChecklistEntity toEntity(Checklist checklist) {
        if (checklist == null) return null;

        ChecklistEntity entity = new ChecklistEntity();
        entity.setId(checklist.getId());
        entity.setVeiculo(VeiculoMapper.toEntity(checklist.getVeiculo()));
        entity.setMecanico(MecanicoMapper.toEntity(checklist.getMecanico()));
        entity.setQuilometragem(checklist.getQuilometragem());
        entity.setDescricao(checklist.getDescricao());
        entity.setStatus(checklist.getStatus());
        entity.setCriadoEm(checklist.getCriadoEm());
        entity.setAtualizadoEm(checklist.getAtualizadoEm());
        
        return entity;
    }

    // Entity → Domain (sem itens)
    public static Checklist toDomain(ChecklistEntity entity) {
        if (entity == null) return null;

        return new Checklist(
            entity.getId(),
            VeiculoMapper.toDomain(entity.getVeiculo()),
            MecanicoMapper.toDomain(entity.getMecanico()),
            entity.getQuilometragem(),
            entity.getDescricao(),
            entity.getStatus(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm()
        );
    }

    // Entity → Domain (com itens)
    public static Checklist toDomain(ChecklistEntity entity, List<ItemChecklistEntity> itensEntity) {
        if (entity == null) return null;

        List<ItemChecklist> itens = itensEntity == null
                ? Collections.emptyList()
                : itensEntity.stream()
                    .map(ItemChecklistMapper::toDomain)
                    .collect(Collectors.toList());

        return new Checklist(
            entity.getId(),
            VeiculoMapper.toDomain(entity.getVeiculo()),
            MecanicoMapper.toDomain(entity.getMecanico()),
            entity.getQuilometragem(),
            entity.getDescricao(),
            entity.getStatus(),
            itens,
            Collections.emptyList(),
            Collections.emptyList(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm()
        );
    }

    // Domain → Response (com agrupamento por categoria)
    public static ChecklistResponse toResponse(Checklist checklist) {
        if (checklist == null) return null;

        Map<CategoriaParteVeiculo, List<ItemChecklistResponse>> itensPorCategoria =
            checklist.getItensChecklist().stream()
                .map(ItemChecklistMapper::toResponse)
                .collect(Collectors.groupingBy(ItemChecklistResponse::parteDoVeiculo));

        return new ChecklistResponse(
            checklist.getId(),
            checklist.getVeiculo().getId(),
            checklist.getStatus(),
            checklist.getCriadoEm(),
            checklist.getAtualizadoEm(),
            itensPorCategoria
        );
    }
}
