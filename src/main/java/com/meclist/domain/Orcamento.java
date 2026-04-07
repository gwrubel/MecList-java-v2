package com.meclist.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.meclist.domain.enums.StatusProcesso;

public class Orcamento {
    private Long id;
    private Checklist checklist;
    private BigDecimal valorTotal;
    private LocalDate dataEmissao;
    private LocalDate dataAprovacao;
    private StatusProcesso status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Orcamento(Long id, Checklist checklist, BigDecimal valorTotal, LocalDate dataEmissao,
                     LocalDate dataAprovacao, StatusProcesso status,
                     LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.checklist = checklist;
        this.valorTotal = valorTotal;
        this.dataEmissao = dataEmissao;
        this.dataAprovacao = dataAprovacao;
        this.status = status;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Orcamento novo(Checklist checklist, BigDecimal valorTotal, StatusProcesso status) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDate hoje = LocalDate.now();
        return new Orcamento(null, checklist, valorTotal, hoje, null, status, agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public Checklist getChecklist() { return checklist; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public LocalDate getDataEmissao() { return dataEmissao; }
    public LocalDate getDataAprovacao() { return dataAprovacao; }
    public StatusProcesso getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setChecklist(Checklist checklist) { this.checklist = checklist; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
    public void setDataAprovacao(LocalDate dataAprovacao) { this.dataAprovacao = dataAprovacao; }
    public void setStatus(StatusProcesso status) { this.status = status; }
   
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    // Métodos de negócio
  

    public void aprovar() {
        this.dataAprovacao = LocalDate.now();
        this.status = StatusProcesso.APROVADO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void rejeitar() {
        this.status = StatusProcesso.CANCELADO;
        this.atualizadoEm = LocalDateTime.now();
    }

   
}





