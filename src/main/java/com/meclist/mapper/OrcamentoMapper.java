package com.meclist.mapper;

import com.meclist.domain.Orcamento;

import com.meclist.persistence.entity.ChecklistEntity;
import com.meclist.persistence.entity.OrcamentoEntity;

public class OrcamentoMapper {

    /**
     * Converte um domínio Orcamento para OrcamentoEntity para persistência.
     * Garante que o checklist não seja nulo e tenha ID válido.
     */
    public static OrcamentoEntity toEntity(Orcamento orcamento) {
        if (orcamento == null) return null;

        OrcamentoEntity entity = new OrcamentoEntity();
        entity.setId(orcamento.getId());

        if (orcamento.getChecklist() == null || orcamento.getChecklist().getId() == null) {
            throw new IllegalArgumentException("Checklist do orçamento não pode ser nulo ou sem ID");
        }
        ChecklistEntity checklistEntity = new ChecklistEntity();
        checklistEntity.setId(orcamento.getChecklist().getId());
        entity.setChecklist(checklistEntity);

        entity.setValorTotal(orcamento.getValorTotal());

        entity.setDataEmissao(orcamento.getDataEmissao() != null
                ? orcamento.getDataEmissao().atStartOfDay() : null);

        entity.setDataAprovacao(orcamento.getDataAprovacao() != null
                ? orcamento.getDataAprovacao().atStartOfDay() : null);

        entity.setStatus(orcamento.getStatus());
        entity.setCriadoEm(orcamento.getCriadoEm());
        entity.setAtualizadoEm(orcamento.getAtualizadoEm());

        return entity;
    }

    /**
     * Converte uma entidade OrcamentoEntity para o domínio Orcamento.
     * O checklist é setado como null, mas pode ser mapeado depois se necessário.
     */
    public static Orcamento toDomain(OrcamentoEntity entity) {
        if (entity == null) return null;

        return new Orcamento(
            entity.getId(),
            null, // Checklist pode ser mapeado depois se necessário
            entity.getValorTotal(),
            entity.getDataEmissao() != null ? entity.getDataEmissao().toLocalDate() : null,
            entity.getDataAprovacao() != null ? entity.getDataAprovacao().toLocalDate() : null,
            entity.getStatus(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm()
        );
    }
}
