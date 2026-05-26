package com.meclist.mapper;

import com.meclist.domain.Servico;
import com.meclist.persistence.entity.ServicoEntity;

public class ServicoMapper {

    public static ServicoEntity toEntity(Servico servico) {
        if (servico == null) {
            return null;
        }

        ServicoEntity entity = new ServicoEntity();
        entity.setId(servico.getId());
        entity.setChecklist(ChecklistMapper.toEntity(servico.getChecklist()));
        entity.setMecanico(MecanicoMapper.toEntity(servico.getMecanico()));
        entity.setDataAtribuicao(servico.getDataAtribuicao());
        entity.setDataInicio(servico.getDataInicio());
        entity.setDataConclusao(servico.getDataConclusao());
        entity.setStatus(servico.getStatus());
        entity.setCriadoEm(servico.getCriadoEm());
        entity.setAtualizadoEm(servico.getAtualizadoEm());
        return entity;
    }

    public static Servico toDomain(ServicoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Servico(
                entity.getId(),
                ChecklistMapper.toDomain(entity.getChecklist()),
                MecanicoMapper.toDomain(entity.getMecanico()),
                entity.getDataAtribuicao(),
                entity.getDataInicio(),
                entity.getDataConclusao(),
                entity.getStatus(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm());
    }
}
