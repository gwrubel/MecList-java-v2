package com.meclist.dto.admin;

import java.util.List;

public record DashboardGraficoDTO(
        List<String> labels,
        List<Long> valores
) {}
