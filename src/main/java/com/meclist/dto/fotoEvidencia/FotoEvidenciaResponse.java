package com.meclist.dto.fotoEvidencia;

import java.time.LocalDateTime;

public record FotoEvidenciaResponse(
    Long id,
    String url,
    LocalDateTime criadoEm
) {

    
}
