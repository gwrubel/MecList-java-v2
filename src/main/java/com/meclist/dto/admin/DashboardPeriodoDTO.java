package com.meclist.dto.admin;

import java.math.BigDecimal;

public record DashboardPeriodoDTO(
        String periodo,
        Long totalServicos,
        Long servicosPendentes,
        Long servicosFinalizados,
        BigDecimal valorTotalMovimentado,
        DashboardGraficoDTO graficoOsPorMes
) {}
