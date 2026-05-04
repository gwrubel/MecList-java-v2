package com.meclist.dto.checklist.aprovacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.EtapaFluxoManual;
import com.meclist.domain.enums.StatusProcesso;

public record ChecklistAprovacaoResponse(
    Long checklistId,
    Long veiculoId,
    String placa,
    String modelo,
    String marca,
    StatusProcesso status,
    EtapaFluxoManual etapaFluxoManual,
    BigDecimal valorTotal,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm,
    Map<CategoriaParteVeiculo, List<ItemAprovacaoResponse>> itensPorCategoria
) {}
