package com.meclist.domain;

import java.time.LocalDateTime;

/**
 * Produto representa o catálogo agnóstico de produtos.
 * Não possui relação direta com checklists ou itens.
 * Serve como base para templates (item_produto) e execuções (checklist_produto).
 */
public class Produto {
    private Long id;
    private String nomeProduto;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Produto(Long id, String nomeProduto, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Produto novo(String nomeProduto) {
        LocalDateTime agora = LocalDateTime.now();
        return new Produto(null, nomeProduto, agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public String getNomeProduto() { return nomeProduto; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    // Métodos de negócio
    public void atualizarNome(String novoNome) {
        this.nomeProduto = novoNome;
        this.atualizadoEm = LocalDateTime.now();
    }
}