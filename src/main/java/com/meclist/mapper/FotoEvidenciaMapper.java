package com.meclist.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.meclist.domain.FotoEvidencia;
import com.meclist.domain.ItemChecklist;
import com.meclist.dto.fotoEvidencia.FotoEvidenciaRequest;
import com.meclist.dto.fotoEvidencia.FotoEvidenciaResponse;
import com.meclist.persistence.entity.FotoEvidenciaEntity;
import com.meclist.persistence.entity.ItemChecklistEntity;

public class FotoEvidenciaMapper {

    // DTO → Domain (usado no use case)
    public static FotoEvidencia toDomain(FotoEvidenciaRequest request, ItemChecklist itemChecklist) {
        return new FotoEvidencia(
            null,
            itemChecklist,
            request.arquivoKey(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    public static List<FotoEvidencia> toDomainList(List<FotoEvidenciaRequest> requests,
                                                   ItemChecklist itemChecklist) {
        return requests.stream()
                .map(request -> toDomain(request, itemChecklist))
                .collect(Collectors.toList());
    }

    // Domain → DTO (resposta)
    public static FotoEvidenciaResponse toResponse(FotoEvidencia foto) {
        return new FotoEvidenciaResponse(
            foto.getId(),
            foto.getPathFoto(),
            foto.getCriadoEm()
        );
    }

    public static List<FotoEvidenciaResponse> toResponseList(List<FotoEvidencia> fotos) {
        return fotos.stream()
                .map(FotoEvidenciaMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Entity → Domain
    public static FotoEvidencia toDomain(FotoEvidenciaEntity entity) {
        return new FotoEvidencia(
            entity.getId(),
            null, // evitamos montar ItemChecklist aqui para não criar ciclo
            entity.getPathFoto(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm()
        );
    }

    public static List<FotoEvidencia> toDomainListFromEntities(List<FotoEvidenciaEntity> entities) {
        return entities.stream()
                .map(FotoEvidenciaMapper::toDomain)
                .collect(Collectors.toList());
    }

    // Domain → Entity (sem associação, caso precise)
    public static FotoEvidenciaEntity toEntity(FotoEvidencia foto) {
        FotoEvidenciaEntity entity = new FotoEvidenciaEntity();
        entity.setId(foto.getId());
        entity.setPathFoto(foto.getPathFoto());
        entity.setCriadoEm(foto.getCriadoEm());
        entity.setAtualizadoEm(foto.getAtualizadoEm());
        return entity;
    }

    // Domain → Entity (com ItemChecklist, ideal para o Gateway)
    public static FotoEvidenciaEntity toEntity(FotoEvidencia foto, ItemChecklistEntity itemChecklistEntity) {
        FotoEvidenciaEntity entity = toEntity(foto);
        entity.setItemChecklist(itemChecklistEntity);
        return entity;
    }
}