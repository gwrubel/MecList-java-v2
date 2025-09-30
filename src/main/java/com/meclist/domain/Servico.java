package com.meclist.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Servico {
    private Long id;
    private Checklist checklist;
    private Mecanico mecanico;
    private LocalDate dataRealizacao;
    private Status status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Servico(Long id, Checklist checklist, Mecanico mecanico, LocalDate dataRealizacao,
                   Status status, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.checklist = checklist;
        this.mecanico = mecanico;
        this.dataRealizacao = dataRealizacao;
        this.status = status;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Servico novo(Checklist checklist, Mecanico mecanico, LocalDate dataRealizacao, Status status) {
        LocalDateTime agora = LocalDateTime.now();
        return new Servico(null, checklist, mecanico, dataRealizacao, status, agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public Checklist getChecklist() { return checklist; }
    public Mecanico getMecanico() { return mecanico; }
    public LocalDate getDataRealizacao() { return dataRealizacao; }
    public Status getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setChecklist(Checklist checklist) { this.checklist = checklist; }
    public void setMecanico(Mecanico mecanico) { this.mecanico = mecanico; }
    public void setDataRealizacao(LocalDate dataRealizacao) { this.dataRealizacao = dataRealizacao; }
    public void setStatus(Status status) { this.status = status; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    // Métodos de negócio
    public void atualizarDataRealizacao(LocalDate novaData) {
        this.dataRealizacao = novaData;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarStatus(Status novoStatus) {
        this.status = novoStatus;
        this.atualizadoEm = LocalDateTime.now();
    }
}





