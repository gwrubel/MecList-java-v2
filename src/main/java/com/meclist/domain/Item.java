package com.meclist.domain;

import java.time.LocalDateTime;

import com.meclist.domain.enums.CategoriaParteVeiculo;

public class Item {
    private Long id;
    private String nome;
    private CategoriaParteVeiculo parteDoVeiculo;
    private String imagemIlustrativa;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Item(Long id, String nome, CategoriaParteVeiculo parteDoVeiculo, String imagemIlustrativa,
                LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.parteDoVeiculo = parteDoVeiculo;
        this.imagemIlustrativa = imagemIlustrativa;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Item novo(String nome, CategoriaParteVeiculo parteDoVeiculo, String imagemIlustrativa) {
        LocalDateTime agora = LocalDateTime.now();
        return new Item(null, nome, parteDoVeiculo, imagemIlustrativa, agora, agora);
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public CategoriaParteVeiculo getParteDoVeiculo() { return parteDoVeiculo; }
    public String getImagemIlustrativa() { return imagemIlustrativa; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}
