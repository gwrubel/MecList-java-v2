package com.meclist.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ItemOrcamento {
    private Long id;
    private Orcamento orcamento;
    private String descricao;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public ItemOrcamento(Long id, Orcamento orcamento, String descricao, Integer quantidade,
                         BigDecimal valorUnitario, BigDecimal valorTotal,
                         LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.orcamento = orcamento;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static ItemOrcamento novo(Orcamento orcamento, String descricao, Integer quantidade, BigDecimal valorUnitario) {
        LocalDateTime agora = LocalDateTime.now();
        BigDecimal valorTotal = valorUnitario.multiply(BigDecimal.valueOf(quantidade));
        return new ItemOrcamento(null, orcamento, descricao, quantidade, valorUnitario, valorTotal, agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public Orcamento getOrcamento() { return orcamento; }
    public String getDescricao() { return descricao; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getValorUnitario() { return valorUnitario; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setOrcamento(Orcamento orcamento) { this.orcamento = orcamento; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    // Métodos de negócio
    public void atualizarQuantidade(Integer novaQuantidade) {
        this.quantidade = novaQuantidade;
        this.calcularValorTotal();
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarValorUnitario(BigDecimal novoValorUnitario) {
        this.valorUnitario = novoValorUnitario;
        this.calcularValorTotal();
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarDescricao(String novaDescricao) {
        this.descricao = novaDescricao;
        this.atualizadoEm = LocalDateTime.now();
    }

    private void calcularValorTotal() {
        if (this.quantidade != null && this.valorUnitario != null) {
            this.valorTotal = this.valorUnitario.multiply(BigDecimal.valueOf(this.quantidade));
        }
    }
}





