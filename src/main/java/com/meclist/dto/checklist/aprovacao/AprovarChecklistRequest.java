package com.meclist.dto.checklist.aprovacao;

import java.util.Collections;
import java.util.List;

import jakarta.validation.Valid;

public record AprovarChecklistRequest(
    @Valid List<AprovarProdutoRequest> produtos
) {
    public AprovarChecklistRequest {
        if (produtos == null) produtos = Collections.emptyList();
    }
}
