package com.meclist.dto.mecanico;

import java.util.List;

public record DashboardMecanicoResponse(
        Long mecanicoId,
        String nomeMecanico,
        List<ServicoCardMecanicoResponse> pendentes,
        List<ServicoCardMecanicoResponse> concluidos
) {
}
