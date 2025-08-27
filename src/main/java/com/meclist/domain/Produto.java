package com.meclist.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Produto {
    private Long id;
    private ItemChecklist itemChecklist;
    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Produto(Long id, ItemChecklist itemChecklist, String nomeProduto, Integer quantidade,
                   BigDecimal valorUnitario, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.itemChecklist = itemChecklist;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    // Construtor para criação inicial sem valor
    public static Produto novo(ItemChecklist itemChecklist, String nomeProduto, Integer quantidade) {
        LocalDateTime agora = LocalDateTime.now();
        return new Produto(null, itemChecklist, nomeProduto, quantidade, null, agora, agora);
    }

    // Construtor para criação com valor
    public static Produto novoComValor(ItemChecklist itemChecklist, String nomeProduto, Integer quantidade, BigDecimal valorUnitario) {
        LocalDateTime agora = LocalDateTime.now();
        return new Produto(null, itemChecklist, nomeProduto, quantidade, valorUnitario, agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public ItemChecklist getItemChecklist() { return itemChecklist; }
    public String getNomeProduto() { return nomeProduto; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getValorUnitario() { return valorUnitario; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setItemChecklist(ItemChecklist itemChecklist) { this.itemChecklist = itemChecklist; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    // Métodos de negócio
    public void atualizarNome(String novoNome) {
        this.nomeProduto = novoNome;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarQuantidade(Integer novaQuantidade) {
        this.quantidade = novaQuantidade;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarValorUnitario(BigDecimal novoValor) {
        this.valorUnitario = novoValor;
        this.atualizadoEm = LocalDateTime.now();
    }

    public BigDecimal getValorTotal() {
        if (this.quantidade != null && this.valorUnitario != null) {
            return this.valorUnitario.multiply(BigDecimal.valueOf(this.quantidade));
        }
        return BigDecimal.ZERO;
    }

    // Método para verificar se tem valor
    public boolean temValor() {
        return this.valorUnitario != null;
    }
}