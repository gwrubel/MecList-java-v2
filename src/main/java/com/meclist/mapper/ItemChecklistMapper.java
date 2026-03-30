package com.meclist.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.meclist.domain.Checklist;
import com.meclist.domain.ItemChecklist;
import com.meclist.dto.fotoEvidencia.FotoEvidenciaResponse;
import com.meclist.dto.itemChecklist.ItemChecklistResponse;
import com.meclist.dto.checklist.precificacao.ItemPrecificacaoResponse;
import com.meclist.dto.checklist.precificacao.ProdutoPrecificadoResponse;
import com.meclist.dto.produto.ProdutoAdicionado;

import com.meclist.persistence.entity.ChecklistEntity;
import com.meclist.persistence.entity.ItemChecklistEntity;
import com.meclist.persistence.entity.ItemEntity;

public class ItemChecklistMapper {

    // ============ Entity → Domain (sem alteração) ============
    public static ItemChecklist toDomain(ItemChecklistEntity entity) {
        if (entity == null) return null;

        Checklist checklist = entity.getChecklist() != null 
            ? ChecklistMapper.toDomain(entity.getChecklist())
            : null;
            
        ItemChecklist ic = new ItemChecklist(
            entity.getId(),
            checklist,
            ItemMapper.toDomain(entity.getItem()),
            entity.getNomeItemSnapshot(),
            entity.getStatusItem(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm()
        );

        if (entity.getFotosEvidencia() != null) {
            entity.getFotosEvidencia().stream()
                .map(FotoEvidenciaMapper::toDomain)
                .forEach(ic::adicionarFotoEvidencia);
        }

        if (entity.getProdutosOrcados() != null) {
            entity.getProdutosOrcados().stream()
                .map(ChecklistProdutoMapper::toDomain)
                .forEach(ic::adicionarProdutoOrcado);
        }

        return ic;
    }

    // ============ MECÂNICO — Response limpo (sem preços) ============
    public static ItemChecklistResponse toResponse(ItemChecklist ic) {
        if (ic == null || ic.getItem() == null) return null;

        List<FotoEvidenciaResponse> fotos = ic.getFotosEvidencia() == null
                ? Collections.emptyList()
                : ic.getFotosEvidencia().stream()
                    .map(f -> new FotoEvidenciaResponse(f.getId(), f.getPathFoto(), f.getCriadoEm()))
                    .collect(Collectors.toList());

        List<ProdutoAdicionado> produtos = ic.getProdutosOrcados() == null
                ? Collections.emptyList()
                : ic.getProdutosOrcados().stream()
                    .map(po -> po.getProduto() == null
                            ? null
                            : new ProdutoAdicionado(po.getProduto().getId(), po.getProduto().getNomeProduto(), po.getQuantidade()))
                    .filter(p -> p != null)
                    .collect(Collectors.toList());

        return new ItemChecklistResponse(
            ic.getId(),
            ic.getItem().getId(),
            ic.getNomeItemSnapshot(),
            ic.getItem().getParteDoVeiculo(),
            ic.getItem().getImagemIlustrativa(),
            ic.getStatusItem(),
            fotos,
            produtos,
            ic.getCriadoEm(),
            ic.getAtualizadoEm()
        );
    }

    // ============ ADM — Response com preços ============
    public static ItemPrecificacaoResponse toPrecificacaoResponse(ItemChecklist ic) {
        if (ic == null || ic.getItem() == null) return null;

        List<FotoEvidenciaResponse> fotos = ic.getFotosEvidencia() == null
                ? Collections.emptyList()
                : ic.getFotosEvidencia().stream()
                    .map(f -> new FotoEvidenciaResponse(f.getId(), f.getPathFoto(), f.getCriadoEm()))
                    .collect(Collectors.toList());

        List<ProdutoPrecificadoResponse> produtos = ic.getProdutosOrcados() == null
                ? Collections.emptyList()
                : ic.getProdutosOrcados().stream()
                    .filter(po -> po.getProduto() != null)
                    .map(po -> new ProdutoPrecificadoResponse(
                            po.getId(),
                            po.getProduto().getId(),
                            po.getNomeProdutoSnapshot(),
                            po.getQuantidade(),
                            po.getValorUnitario(),
                            po.getMarca()))
                    .collect(Collectors.toList());

        return new ItemPrecificacaoResponse(
            ic.getId(),
            ic.getNomeItemSnapshot(),
            ic.getItem().getId(),
            ic.getItem().getParteDoVeiculo(),
            ic.getStatusItem(),
            fotos,
            produtos,
            ic.getMaoDeObra()
        );
    }

    // ============ Listas ============
    public static List<ItemChecklistResponse> toResponse(List<ItemChecklist> list) {
        if (list == null) return Collections.emptyList();
        return list.stream()
                   .map(ItemChecklistMapper::toResponse)
                   .collect(Collectors.toList());
    }

    public static List<ItemPrecificacaoResponse> toPrecificacaoResponse(List<ItemChecklist> list) {
        if (list == null) return Collections.emptyList();
        return list.stream()
                   .map(ItemChecklistMapper::toPrecificacaoResponse)
                   .collect(Collectors.toList());
    }

    // ============ Domain → Entity (sem alteração) ============
    public static ItemChecklistEntity toEntity(ItemChecklist ic) {
        if (ic == null) return null;

        ItemChecklistEntity entity = new ItemChecklistEntity();
        entity.setId(ic.getId());

        if (ic.getChecklist() != null && ic.getChecklist().getId() != null) {
            ChecklistEntity checklistEntity = new ChecklistEntity();
            checklistEntity.setId(ic.getChecklist().getId());
            entity.setChecklist(checklistEntity);
        }

        if (ic.getItem() != null && ic.getItem().getId() != null) {
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setId(ic.getItem().getId());
            entity.setItem(itemEntity);
        }

        entity.setStatusItem(ic.getStatusItem());
        entity.setCriadoEm(ic.getCriadoEm());
        entity.setAtualizadoEm(ic.getAtualizadoEm());
        entity.setNomeItemSnapshot(ic.getNomeItemSnapshot());
        entity.setMaoDeObra(ic.getMaoDeObra());

        return entity;
    }
}
