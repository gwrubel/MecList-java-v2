package com.meclist.dto.checklist.precificacao;

import java.math.BigDecimal;
import java.util.List;

public record PrecificarItemRequest(
    Long itemChecklistId,
    BigDecimal maoDeObra,
    List <PrecificarProdutoRequest> produtos 
) {

}
