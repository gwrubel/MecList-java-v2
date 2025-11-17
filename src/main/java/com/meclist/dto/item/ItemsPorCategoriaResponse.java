package com.meclist.dto.item;

import java.util.List;

import com.meclist.domain.enums.CategoriaParteVeiculo;

public record ItemsPorCategoriaResponse(
    CategoriaParteVeiculo categoria,
    List<ItemResponse> itens
) {}