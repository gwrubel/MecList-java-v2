package com.meclist.domain;

import java.time.LocalDateTime;

import com.meclist.domain.enums.Situacao;

/**
 * Produto representa o catálogo agnóstico de produtos.
 * Não possui relação direta com checklists ou itens.
 * Serve como base para templates (item_produto) e execuções (checklist_produto).
 */
public class Produto {
    private Long id;
    private String nomeProduto;
    private Situacao situacao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Produto(Long id, String nomeProduto, Situacao situacao, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.situacao = situacao;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Produto novo(String nomeProduto) {
        LocalDateTime agora = LocalDateTime.now();
        Situacao situacao = Situacao.ATIVO;
        return new Produto(null, nomeProduto,situacao ,agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public String getNomeProduto() { return nomeProduto; }
    public Situacao getSituacao() { return situacao; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
    public void setSituacao(Situacao situacao) { this.situacao = situacao; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    // Métodos de negócio
    public void atualizarNome(String novoNome) {
        this.nomeProduto = novoNome;
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