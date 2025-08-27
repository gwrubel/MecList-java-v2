package com.meclist.dto.checklist;

public record CriarChecklistRequest(
    Long idVeiculo,
    Long idMecanico,
    Float quilometragem,
    String descricao,
    Long idStatus
) {}



