package com.meclist.dto.admin;

import java.math.BigDecimal;
import java.util.List;


public record DashboardAdmResponse(
        DashboardPeriodoDTO ultimos7Dias,
        DashboardPeriodoDTO ultimos30Dias,
        DashboardEstadoAtualDTO estadoAtual,
        BigDecimal ticketMedio,
        List<MecanicoTopDTO> topMecanicos,
        DashboardTemposMediosDTO temposMedios
) {}
