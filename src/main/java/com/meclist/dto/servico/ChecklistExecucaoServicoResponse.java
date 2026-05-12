package com.meclist.dto.servico;

import java.util.List;
import java.util.Map;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.StatusProcesso;

public record ChecklistExecucaoServicoResponse(
        Long servicoId,
        Long checklistId,
        StatusProcesso statusServico,
        StatusProcesso statusChecklist,
        Long veiculoId,
        String placa,
        String cor,
        String marca,
        String modelo,
        Integer ano,
        Float quilometragem,
        String descricaoChecklist,
        Map<CategoriaParteVeiculo, List<ItemExecucaoServicoResponse>> itensPorCategoria
) {
}
