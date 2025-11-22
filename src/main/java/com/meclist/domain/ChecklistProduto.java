package com.meclist.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ChecklistProduto representa o relacionamento entre ItemChecklist e Produto na execução/orçamento.
 * Registra os produtos efetivamente orçados para um checklist específico de um cliente.
 * 
 * aprovado_cliente: NULL = Pendente, TRUE = Aprovado, FALSE = Rejeitado (tri-state)
 */
public class ChecklistProduto {
    private Long id;
    private ItemChecklist itemChecklist;
    private Produto produto;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private Boolean aprovadoCliente; // NULL = Pendente, TRUE = Aprovado, FALSE = Rejeitado
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public ChecklistProduto(Long id, ItemChecklist itemChecklist, Produto produto, Integer quantidade,
                           BigDecimal valorUnitario, Boolean aprovadoCliente, 
                           LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.itemChecklist = itemChecklist;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.aprovadoCliente = aprovadoCliente;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    // Construtor para criação inicial sem valor
    public static ChecklistProduto novo(ItemChecklist itemChecklist, Produto produto, Integer quantidade) {
        LocalDateTime agora = LocalDateTime.now();
        return new ChecklistProduto(null, itemChecklist, produto, quantidade, null, null, agora, agora);
    }

    // Construtor para criação com valor
    public static ChecklistProduto novoComValor(ItemChecklist itemChecklist, Produto produto, 
                                                Integer quantidade, BigDecimal valorUnitario) {
        LocalDateTime agora = LocalDateTime.now();
        return new ChecklistProduto(null, itemChecklist, produto, quantidade, valorUnitario, null, agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public ItemChecklist getItemChecklist() { return itemChecklist; }
    public Produto getProduto() { return produto; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getValorUnitario() { return valorUnitario; }
    public Boolean getAprovadoCliente() { return aprovadoCliente; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setItemChecklist(ItemChecklist itemChecklist) { this.itemChecklist = itemChecklist; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
    public void setAprovadoCliente(Boolean aprovadoCliente) { this.aprovadoCliente = aprovadoCliente; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    // Métodos de negócio
    public void atualizarQuantidade(Integer novaQuantidade) {
        this.quantidade = novaQuantidade;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarValorUnitario(BigDecimal novoValor) {
        this.valorUnitario = novoValor;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void aprovar() {
        this.aprovadoCliente = true;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void rejeitar() {
        this.aprovadoCliente = false;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void pendente() {
        this.aprovadoCliente = null;
        this.atualizadoEm = LocalDateTime.now();
    }

    public boolean estaPendente() {
        return this.aprovadoCliente == null;
    }

    public boolean estaAprovado() {
        return Boolean.TRUE.equals(this.aprovadoCliente);
    }

    public boolean estaRejeitado() {
        return Boolean.FALSE.equals(this.aprovadoCliente);
    }

    public BigDecimal getValorTotal() {
        if (this.quantidade != null && this.valorUnitario != null) {
            return this.valorUnitario.multiply(BigDecimal.valueOf(this.quantidade));
        }
        return BigDecimal.ZERO;
    }
}

