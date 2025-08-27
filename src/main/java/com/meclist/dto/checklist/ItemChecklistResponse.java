package com.meclist.dto.checklist;

public record ItemChecklistResponse(
    Long id,
    Long idChecklist,
    Long idItem,
    String nomeItem,
    String imagemIlustrativa,
    Long idStatusItem,
    String descricaoStatus
) {}