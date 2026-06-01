package com.meclist.dto.checklist.visualizacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.StatusProcesso;

public record ChecklistVisualizacaoCompletaResponse(
        Long checklistId,
        Long veiculoId,
        String placa,
        String marca,
        String modelo,
        Integer ano,
        String cor,
        Float quilometragem,
        String nomeCliente,
        String nomeMecanico,
        StatusProcesso status,
        BigDecimal valorTotal,
        LocalDateTime criadoEm,
        LocalDateTime DataConclusao,
        Map<CategoriaParteVeiculo, List<ItemVisualizacaoCompletaResponse>> itensPorCategoria
) {
}
