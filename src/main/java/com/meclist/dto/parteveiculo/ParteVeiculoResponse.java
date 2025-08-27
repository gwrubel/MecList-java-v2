package com.meclist.dto.parteveiculo;

import com.meclist.domain.enums.CategoriaParteVeiculo;

public record ParteVeiculoResponse(
    Long id,
    String nome,
    String imagem,
    CategoriaParteVeiculo categoriaParteVeiculo
) {}
