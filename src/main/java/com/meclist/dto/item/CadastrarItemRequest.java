package com.meclist.dto.item;

import com.meclist.domain.enums.CategoriaParteVeiculo;

public record CadastrarItemRequest(
    String nome,
    CategoriaParteVeiculo parteDoVeiculo
) {}
