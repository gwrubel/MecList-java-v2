package com.meclist.dto.admin;

import java.math.BigDecimal;

public record DashboardPeriodoDTO(
        String periodo,
        Long movimentados,
        Long finalizados,
        BigDecimal faturamento,
        Double taxaAprovacao,
        DashboardGraficoDTO grafico
) {}
