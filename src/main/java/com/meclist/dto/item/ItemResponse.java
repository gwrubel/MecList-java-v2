package com.meclist.dto.item;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.Situacao;

import java.time.LocalDateTime;


public record ItemResponse(
    Long id,
    String nome,
    CategoriaParteVeiculo parteDoVeiculo,
    String imagemIlustrativa,
    Situacao situacao,
    Integer quantidadeProdutos,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {
    public ItemResponse(Long id, String nome, CategoriaParteVeiculo parteDoVeiculo, String imagemIlustrativa, Situacao situacao, Integer quantidadeProdutos) {
        this(id, nome, parteDoVeiculo, imagemIlustrativa,situacao, quantidadeProdutos, null, null);
    }
}





