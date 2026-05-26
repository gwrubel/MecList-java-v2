package com.meclist.dto.cliente;

import java.time.LocalDateTime;

public record HistoricoServicoCard(
        Long checklistId,
        String descricao,
        Float quilometragem,
        LocalDateTime dataConclusao,
        String mecanicoResponsavel
) {
}
