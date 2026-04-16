package com.meclist.dto.cliente;

import java.time.LocalDate;

public record VeiculoResumo(
    Long id,
    String marca,
    String modelo,
    String placa,
    Float quilometragem,
    LocalDate dataUltimaRevisao
) {
    
}
