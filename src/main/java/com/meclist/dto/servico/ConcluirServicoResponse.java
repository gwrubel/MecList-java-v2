package com.meclist.dto.servico;

import com.meclist.domain.enums.StatusProcesso;

public record ConcluirServicoResponse(
        Long servicoId,
        Long checklistId,
        int itensMarcadosComoTrocaFeita,
        int itensSemAutorizacao,
        StatusProcesso statusServico,
        StatusProcesso statusChecklist
) {
}
