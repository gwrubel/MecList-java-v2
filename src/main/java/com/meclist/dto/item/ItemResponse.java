package com.meclist.dto.item;

import com.meclist.domain.enums.CategoriaParteVeiculo;

import java.time.LocalDateTime;


public record ItemResponse(
    Long id,
    String nome,
    CategoriaParteVeiculo parteDoVeiculo,
    String imagemIlustrativa,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {}





