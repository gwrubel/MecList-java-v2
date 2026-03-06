package com.meclist.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.Situacao;

public class Item {
    private Long id;
    private String nome;
    private CategoriaParteVeiculo parteDoVeiculo;
    private String imagemIlustrativa;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private List<Produto> produtos = new ArrayList<>();
    private Situacao situacao; // alterar situação do item (ativo/inativo)

    public void adicionarProduto(Produto produto) {
        if (this.produtos == null) {
            this.produtos = new ArrayList<Produto>();
        }
        this.produtos.add(produto);
    }
    
    public List<Produto> getProdutos() {
        return produtos != null ? produtos : new ArrayList<Produto>();
    }

    public Item(Long id, String nome, CategoriaParteVeiculo parteDoVeiculo, String imagemIlustrativa,
                LocalDateTime criadoEm, LocalDateTime atualizadoEm, Situacao situacao) {
        this.id = id;
        this.nome = nome;
        this.parteDoVeiculo = parteDoVeiculo;
        this.imagemIlustrativa = imagemIlustrativa;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.situacao = situacao;
    }

    public static Item novo(String nome, CategoriaParteVeiculo parteDoVeiculo, String imagemIlustrativa) {
        LocalDateTime agora = LocalDateTime.now();
        return new Item(null, nome, parteDoVeiculo, imagemIlustrativa, agora, agora, Situacao.ATIVO);
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public CategoriaParteVeiculo getParteDoVeiculo() { return parteDoVeiculo; }
    public String getImagemIlustrativa() { return imagemIlustrativa; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public Situacao getSituacao() { return situacao; }

    public void atualizarItem(String nome, CategoriaParteVeiculo parteDoVeiculo, String imagemIlustrativa) {
        this.nome = nome;
        this.parteDoVeiculo = parteDoVeiculo;
        this.imagemIlustrativa = imagemIlustrativa;
       
        this.atualizadoEm = LocalDateTime.now();
    }


    public void ativar() {
        this.situacao = Situacao.ATIVO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void desativar() {
        this.situacao = Situacao.INATIVO;
        this.atualizadoEm = LocalDateTime.now();
    }
}
