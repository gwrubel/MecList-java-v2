package com.meclist.dto.checklist;

import java.util.List;

import com.meclist.domain.enums.CategoriaParteVeiculo;

public record ItemsPorCategoriaResponse(
    CategoriaParteVeiculo categoria,
    List<ItemResponse> itens
) {}