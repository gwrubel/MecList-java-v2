package com.meclist.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.meclist.domain.ItemChecklist;
import com.meclist.dto.fotoEvidencia.FotoEvidenciaResponse;
import com.meclist.dto.itemChecklist.ItemChecklistResponse;
import com.meclist.dto.produto.ProdutoResponse;
import com.meclist.persistence.entity.ItemChecklistEntity;

public class ItemChecklistMapper {

    public static ItemChecklist toDomain(ItemChecklistEntity entity) {
        if (entity == null) return null;

        return new ItemChecklist(
            entity.getId(),
            null,
            ItemMapper.toDomain(entity.getItem()),
            entity.getStatusItem(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm()
        );
    }

    public static ItemChecklistResponse toResponse(ItemChecklist ic) {
        if (ic == null || ic.getItem() == null) return null;

        List<FotoEvidenciaResponse> fotos = ic.getFotosEvidencia() == null
                ? Collections.emptyList()
                : ic.getFotosEvidencia().stream()
                    .map(f -> new FotoEvidenciaResponse(f.getId(), f.getUrlFoto(), f.getCriadoEm()))
                    .collect(Collectors.toList());


         
        List<ProdutoResponse> produtos = ic.getProdutosOrcados() == null
                ? Collections.emptyList()
                : ic.getProdutosOrcados().stream()
                    .map(po -> po.getProduto() == null
                            ? null
                            : new ProdutoResponse(po.getProduto().getId(), po.getProduto().getNomeProduto()))
                    .filter(p -> p != null)
                    .collect(Collectors.toList());
      

        return new ItemChecklistResponse(
            ic.getId(),
            ic.getItem().getId(),
            ic.getItem().getNome(),
            ic.getItem().getParteDoVeiculo(),
            ic.getItem().getImagemIlustrativa(),
            ic.getStatusItem(),
            fotos,
            produtos,
            ic.getCriadoEm(),
            ic.getAtualizadoEm()
        );
    }

    public static List<ItemChecklistResponse> toResponse(List<ItemChecklist> list) {
        if (list == null) return Collections.emptyList();
        return list.stream()
                   .map(ItemChecklistMapper::toResponse)
                   .collect(Collectors.toList());
    }
}
