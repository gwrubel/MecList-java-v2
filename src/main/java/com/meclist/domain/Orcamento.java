package com.meclist.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Orcamento {
    private Long id;
    private Checklist checklist;
    private BigDecimal valorTotal;
    private LocalDate dataEmissao;
    private LocalDate dataAprovacao;
    private Status status;
    private List<ItemOrcamento> itens;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Orcamento(Long id, Checklist checklist, BigDecimal valorTotal, LocalDate dataEmissao,
                     LocalDate dataAprovacao, Status status, List<ItemOrcamento> itens,
                     LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.checklist = checklist;
        this.valorTotal = valorTotal;
        this.dataEmissao = dataEmissao;
        this.dataAprovacao = dataAprovacao;
        this.status = status;
        this.itens = itens != null ? itens : new ArrayList<>();
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Orcamento novo(Checklist checklist, BigDecimal valorTotal, Status status) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDate hoje = LocalDate.now();
        return new Orcamento(null, checklist, valorTotal, hoje, null, status, new ArrayList<>(), agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public Checklist getChecklist() { return checklist; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public LocalDate getDataEmissao() { return dataEmissao; }
    public LocalDate getDataAprovacao() { return dataAprovacao; }
    public Status getStatus() { return status; }
    public List<ItemOrcamento> getItens() { return itens; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setChecklist(Checklist checklist) { this.checklist = checklist; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
    public void setDataAprovacao(LocalDate dataAprovacao) { this.dataAprovacao = dataAprovacao; }
    public void setStatus(Status status) { this.status = status; }
    public void setItens(List<ItemOrcamento> itens) { this.itens = itens; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    // Métodos de negócio
    public void adicionarItem(ItemOrcamento item) {
        if (this.itens == null) {
            this.itens = new ArrayList<>();
        }
        this.itens.add(item);
        this.atualizarValorTotal();
        this.atualizadoEm = LocalDateTime.now();
    }

    public void removerItem(ItemOrcamento item) {
        if (this.itens != null) {
            this.itens.remove(item);
            this.atualizarValorTotal();
            this.atualizadoEm = LocalDateTime.now();
        }
    }

    public void aprovar() {
        this.dataAprovacao = LocalDate.now();
        this.status = new Status(2L, "Aprovado", LocalDateTime.now(), LocalDateTime.now());
        this.atualizadoEm = LocalDateTime.now();
    }

    public void rejeitar() {
        this.status = new Status(3L, "Rejeitado", LocalDateTime.now(), LocalDateTime.now());
        this.atualizadoEm = LocalDateTime.now();
    }

    private void atualizarValorTotal() {
        if (this.itens != null) {
            this.valorTotal = this.itens.stream()
                .map(ItemOrcamento::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}





