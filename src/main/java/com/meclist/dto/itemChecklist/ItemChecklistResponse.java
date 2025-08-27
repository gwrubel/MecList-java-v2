package com.meclist.dto.itemChecklist;

import java.time.LocalDateTime;
import java.util.List;

import com.meclist.dto.fotoEvidencia.FotoEvidenciaResponse;

public record ItemChecklistResponse(
    Long id,
    Long idChecklist,
    Long idItem,
    Long idStatusItem,
    List<FotoEvidenciaResponse> fotosEvidencia,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {}
