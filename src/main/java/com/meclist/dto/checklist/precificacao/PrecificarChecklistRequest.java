package com.meclist.dto.checklist.precificacao;

import java.util.List;

public record PrecificarChecklistRequest(
    List<PrecificarItemRequest> itens
) {
    
}
