package com.meclist.dto.fotoEvidencia;


public record FotoEvidenciaRequest(
    Long id,
    String arquivoKey  // foto nova (upload)
) {
}
