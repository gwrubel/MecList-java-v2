package com.meclist.infra;

import java.time.LocalDateTime;

public record ServicoConcluidoEvent(
        Long servicoId,
        Long checklistId,
        String emailCliente,
        String nomeCliente,
        String placa,
        String marca,
        String modelo,
        LocalDateTime concluidoEm
) {}
