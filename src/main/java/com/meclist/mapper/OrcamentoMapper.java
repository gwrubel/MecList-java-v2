package com.meclist.mapper;

import com.meclist.domain.Orcamento;
import com.meclist.persistence.entity.ChecklistEntity;
import com.meclist.persistence.entity.OrcamentoEntity;


public class OrcamentoMapper {

    public static OrcamentoEntity toEntity(Orcamento orcamento) {
        if (orcamento == null) return null;

        OrcamentoEntity entity = new OrcamentoEntity();
        entity.setId(orcamento.getId());

        if (orcamento.getChecklist() != null) {
            ChecklistEntity checklistEntity = new ChecklistEntity();
            checklistEntity.setId(orcamento.getChecklist().getId());
            entity.setChecklist(checklistEntity);
        }

        entity.setValorTotal(orcamento.getValorTotal());

        entity.setDataEmissao(orcamento.getDataEmissao() != null
                ? orcamento.getDataEmissao().atStartOfDay() : null);

        entity.setDataAprovacao(orcamento.getDataAprovacao() != null
                ? orcamento.getDataAprovacao().atStartOfDay() : null);

        if (orcamento.getStatus() != null) {
            entity.setStatus(orcamento.getStatus());
        }

        entity.setCriadoEm(orcamento.getCriadoEm());
        entity.setAtualizadoEm(orcamento.getAtualizadoEm());
        return entity;
    }

    public static Orcamento toDomain(OrcamentoEntity entity) {
        if (entity == null) return null;

        return new Orcamento(
            entity.getId(),
            null,
            entity.getValorTotal(),
            entity.getDataEmissao() != null ? entity.getDataEmissao().toLocalDate() : null,
            entity.getDataAprovacao() != null ? entity.getDataAprovacao().toLocalDate() : null,
            entity.getStatus(),
            null,
            entity.getCriadoEm(),
            entity.getAtualizadoEm()
        );
    }
}
