package com.meclist.dto.checklist.precificacao;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PrecificarItemRequest(
    @NotNull(message = "itemChecklistId é obrigatório")
    Long itemChecklistId,
    BigDecimal maoDeObra,
    @NotEmpty(message = "Cada item deve conter ao menos um produto na precificação")
    List<@Valid PrecificarProdutoRequest> produtos
) {

}
