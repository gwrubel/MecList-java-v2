package com.meclist.dto.checklist;

import com.meclist.domain.enums.CategoriaParteVeiculo;

public record ItemResponse(
    Long id,
    String nome,
    CategoriaParteVeiculo parteDoVeiculo,
    String imagemIlustrativa
) {}
