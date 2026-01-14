package com.meclist.dto.item;


import com.meclist.domain.enums.CategoriaParteVeiculo;

public record AtualizarItemRequest(
    String nome,
    CategoriaParteVeiculo parteDoVeiculo
) {}
