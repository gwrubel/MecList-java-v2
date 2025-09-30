package com.meclist.dto.statusItem;

import java.time.LocalDateTime;

public record StatusItemResponse(
    Long id,
    String descricao,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {}





