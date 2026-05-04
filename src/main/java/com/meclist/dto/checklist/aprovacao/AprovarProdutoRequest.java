package com.meclist.dto.checklist.aprovacao;

import jakarta.validation.constraints.NotNull;

public record AprovarProdutoRequest(
    @NotNull Long checklistProdutoId,
    @NotNull Boolean aprovadoCliente
) {}
