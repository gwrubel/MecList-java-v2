package com.meclist.dto.checklist;

import java.time.LocalDateTime;

import com.meclist.domain.enums.StatusProcesso;

public record ChecklistResumoResponse(
    Long checklistId,
    Long veiculoId,
    String placa,
    String nomeCliente,
    StatusProcesso status,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {}
