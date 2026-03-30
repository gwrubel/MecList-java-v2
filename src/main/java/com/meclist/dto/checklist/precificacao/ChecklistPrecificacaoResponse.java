package com.meclist.dto.checklist.precificacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.StatusProcesso;

public record ChecklistPrecificacaoResponse(
    Long checklistId,
    Long veiculoId,
    String placa,
    String nomeCliente,
    StatusProcesso status,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm,
    Map<CategoriaParteVeiculo, List<ItemPrecificacaoResponse>> itensPorCategoria
) {}
