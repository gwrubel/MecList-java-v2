package com.meclist.dto.cliente;


import com.meclist.domain.enums.StatusProcesso;

public record ChecklistCardResumo(
    Long id,
    String placa,
    String marca,
    String modelo,
    Integer ano,
    String descricao,
    String nomeMecanico,
    Float quilometragem,
    StatusProcesso status
) {
    
}
