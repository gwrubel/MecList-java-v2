package com.meclist.dto.checklist.precificacao;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record PrecificarChecklistRequest(
    @NotEmpty(message = "A precificação deve conter ao menos um item")
    List<@Valid PrecificarItemRequest> itens
) {
    
}
