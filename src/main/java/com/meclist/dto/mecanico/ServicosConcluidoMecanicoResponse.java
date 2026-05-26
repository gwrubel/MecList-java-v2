package com.meclist.dto.mecanico;

import java.time.LocalDateTime;

public record ServicosConcluidoMecanicoResponse(
        Long checklistId,
        String nomeCliente,
        String placa,
        String modelo,
        LocalDateTime dataInicio,
        LocalDateTime dataConclusao
) {
}