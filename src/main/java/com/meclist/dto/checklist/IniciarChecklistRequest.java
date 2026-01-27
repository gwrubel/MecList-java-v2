package com.meclist.dto.checklist;

public record IniciarChecklistRequest(
    Long idVeiculo,
    Long idMecanico,
    Float quilometragem,
    String descricao
) {
    
}
