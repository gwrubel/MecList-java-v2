package com.meclist.dto.admin;

import java.math.BigDecimal;
import java.util.List;

public record DashboardAdmResponse(
        DashboardPeriodoDTO ultimos7Dias,
        DashboardPeriodoDTO ultimos30Dias,
        BigDecimal ticketMedio,
        Double taxaAprovacao,
        List<MecanicoTopDTO> topMecanicos
) {}
