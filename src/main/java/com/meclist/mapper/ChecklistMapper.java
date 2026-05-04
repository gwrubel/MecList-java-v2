package com.meclist.mapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.meclist.domain.Checklist;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.Orcamento;
import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.EtapaFluxoManual;
import com.meclist.domain.enums.StatusItem;
import com.meclist.dto.checklist.ChecklistResponse;
import com.meclist.dto.checklist.ChecklistResumoResponse;
import com.meclist.dto.checklist.aprovacao.ChecklistAprovacaoResponse;
import com.meclist.dto.checklist.aprovacao.ItemAprovacaoResponse;
import com.meclist.dto.checklist.precificacao.ChecklistPrecificacaoResponse;
import com.meclist.dto.checklist.precificacao.ItemPrecificacaoResponse;
import com.meclist.dto.itemChecklist.ItemChecklistResponse;
import com.meclist.persistence.entity.ChecklistEntity;
import com.meclist.persistence.entity.ItemChecklistEntity;

public class ChecklistMapper {

    // Domain → Entity
   // No ChecklistMapper.java

public static ChecklistEntity toEntity(Checklist domain) {
    if (domain == null) return null;

    ChecklistEntity entity = new ChecklistEntity();
    entity.setId(domain.getId());
    entity.setVeiculo(VeiculoMapper.toEntity(domain.getVeiculo()));
    entity.setMecanico(MecanicoMapper.toEntity(domain.getMecanico()));
    entity.setQuilometragem(domain.getQuilometragem());
    entity.setDescricao(domain.getDescricao());
    entity.setStatus(domain.getStatus());
    entity.setOrigemAprovacao(domain.getOrigemAprovacao());
    entity.setAprovadoPorId(domain.getAprovadoPorId());
    entity.setAprovadoPorTipo(domain.getAprovadoPorTipo());
    entity.setAprovadoEm(domain.getAprovadoEm());
    entity.setCriadoEm(domain.getCriadoEm());
    entity.setAtualizadoEm(domain.getAtualizadoEm());

    // ADICIONE ISSO: Mapeia os itens de volta para a entidade
    if (domain.getItensChecklist() != null) {
        List<ItemChecklistEntity> itensEntities = domain.getItensChecklist().stream()
            .map(itemDomain -> {
                ItemChecklistEntity itemEntity = ItemChecklistMapper.toEntity(itemDomain);
                itemEntity.setChecklist(entity); // CRUCIAL: Vincula o filho ao pai
                return itemEntity;
            })
            .collect(Collectors.toList());
        entity.setItensChecklist(itensEntities);
    }

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
            entity.getOrigemAprovacao(),
            entity.getAprovadoPorId(),
            entity.getAprovadoPorTipo(),
            entity.getAprovadoEm(),
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
            entity.getOrigemAprovacao(),
            entity.getAprovadoPorId(),
            entity.getAprovadoPorTipo(),
            entity.getAprovadoEm(),
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

    public static ChecklistPrecificacaoResponse toPrecificacaoResponse(Checklist checklist) {
    if (checklist == null) return null;

    Map<CategoriaParteVeiculo, List<ItemPrecificacaoResponse>> itensPorCategoria =
        checklist.getItensChecklist().stream()
            .map(ItemChecklistMapper::toPrecificacaoResponse)
            .collect(Collectors.groupingBy(ItemPrecificacaoResponse::parteDoVeiculo));

    return new ChecklistPrecificacaoResponse(
        checklist.getId(),
        checklist.getVeiculo().getId(),
        checklist.getVeiculo().getPlaca(),
        checklist.getVeiculo().getCliente().getNome(),
        checklist.getStatus(),
        checklist.getCriadoEm(),
        checklist.getAtualizadoEm(),
        itensPorCategoria
    );
}

    public static ChecklistPrecificacaoResponse toPrecificacaoResponse(Checklist checklist, 
                                                                     List<ItemChecklist> itens) {
    if (checklist == null) return null;

    Map<CategoriaParteVeiculo, List<ItemPrecificacaoResponse>> itensPorCategoria =
        itens.stream()
            .map(ItemChecklistMapper::toPrecificacaoResponse)
            .collect(Collectors.groupingBy(ItemPrecificacaoResponse::parteDoVeiculo));

    return new ChecklistPrecificacaoResponse(
        checklist.getId(),
        checklist.getVeiculo().getId(),
        checklist.getVeiculo().getPlaca(),
        checklist.getVeiculo().getCliente().getNome(),
        checklist.getStatus(),
        checklist.getCriadoEm(),
        checklist.getAtualizadoEm(),
        itensPorCategoria
    );
}

    public static ChecklistResumoResponse toResumoResponse(Checklist checklist) {
    if (checklist == null) return null;

    String nomeCliente = checklist.getVeiculo().getCliente() != null
            ? checklist.getVeiculo().getCliente().getNome()
            : null;

    return new ChecklistResumoResponse(
        checklist.getId(),
        checklist.getVeiculo().getId(),
        checklist.getVeiculo().getPlaca(),
        nomeCliente,
        checklist.getStatus(),
        checklist.getCriadoEm(),
        checklist.getAtualizadoEm()
    );
}

    public static ChecklistAprovacaoResponse toAprovacaoResponse(Checklist checklist, BigDecimal valorTotal) {
        return toAprovacaoResponse(checklist, valorTotal, null);
    }

    public static ChecklistAprovacaoResponse toAprovacaoResponse(Checklist checklist,
                                                                 BigDecimal valorTotal,
                                                                 EtapaFluxoManual etapaFluxoManual) {
        if (checklist == null) return null;

        Map<CategoriaParteVeiculo, List<ItemAprovacaoResponse>> itensPorCategoria =
            checklist.getItensChecklist().stream()
                .filter(ic -> ic.getStatusItem() == StatusItem.TROCAR)
                .map(ItemChecklistMapper::toAprovacaoResponse)
                .collect(Collectors.groupingBy(ItemAprovacaoResponse::parteDoVeiculo));

        return new ChecklistAprovacaoResponse(
            checklist.getId(),
            checklist.getVeiculo().getId(),
            checklist.getVeiculo().getPlaca(),
            checklist.getVeiculo().getModelo(),
            checklist.getVeiculo().getMarca(),

            checklist.getStatus(),
            etapaFluxoManual,
            valorTotal,
            checklist.getCriadoEm(),
            checklist.getAtualizadoEm(),
            itensPorCategoria
        );
    }
}
