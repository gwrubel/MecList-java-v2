package com.meclist.dto.veiculo;

import java.time.LocalDateTime;

public record HistoricoServicoCard(
        Long checklistId,
        String descricao,
        Float quilometragem,
        LocalDateTime dataConclusao,
        String mecanicoResponsavel
) {
}
