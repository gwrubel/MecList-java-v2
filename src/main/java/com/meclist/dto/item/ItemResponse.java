package com.meclist.dto.item;

import com.meclist.domain.enums.CategoriaParteVeiculo;

import java.time.LocalDateTime;


public record ItemResponse(
    Long id,
    String nome,
    CategoriaParteVeiculo parteDoVeiculo,
    String imagemIlustrativa,
    Integer quantidadeProdutos,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {
    public ItemResponse(Long id, String nome, CategoriaParteVeiculo parteDoVeiculo, String imagemIlustrativa, Integer quantidadeProdutos) {
        this(id, nome, parteDoVeiculo, imagemIlustrativa, quantidadeProdutos, null, null);
    }
}





