package com.meclist.dto.servico;

import java.time.LocalDateTime;

import com.meclist.domain.enums.StatusProcesso;

public record IniciarServicoResponse(
        Long servicoId,
        Long checklistId,
        StatusProcesso statusServico,
        StatusProcesso statusChecklist,
        LocalDateTime dataInicio) {
}
