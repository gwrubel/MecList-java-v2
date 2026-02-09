package com.meclist.dto.veiculo;

import java.time.LocalDate;

public record VeiculoBuscaResponse(
    Long id,
    String placa,
    String modelo,
    String marca,
    String cor,
    Integer ano,
    float quilometragem,
    LocalDate dataUltimaRevisao
) {
    
}
