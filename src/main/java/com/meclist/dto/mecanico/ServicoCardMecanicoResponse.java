package com.meclist.dto.mecanico;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.meclist.domain.enums.StatusProcesso;

public record ServicoCardMecanicoResponse(
        Long servicoId,
        Long checklistId,
        StatusProcesso statusServico,
        Long veiculoId,
        String placa,
        String marca,
        String modelo,
        Integer ano,
        Float quilometragem,
        LocalDateTime criadoEm,
        LocalDate dataConclusao
) {
}
