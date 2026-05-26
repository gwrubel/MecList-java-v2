package com.meclist.domain;

import java.time.LocalDateTime;

import com.meclist.domain.enums.StatusProcesso;

public class Servico {
    private Long id;
    private Checklist checklist;
    private Mecanico mecanico;
    private LocalDateTime dataAtribuicao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataConclusao;
    private StatusProcesso status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Servico(Long id, Checklist checklist, Mecanico mecanico,
                   LocalDateTime dataAtribuicao, LocalDateTime dataInicio, LocalDateTime dataConclusao,
                   StatusProcesso status, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.checklist = checklist;
        this.mecanico = mecanico;
        this.dataAtribuicao = dataAtribuicao;
        this.dataInicio = dataInicio;
        this.dataConclusao = dataConclusao;
        this.status = status;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Servico novo(Checklist checklist, Mecanico mecanico, StatusProcesso status) {
        LocalDateTime agora = LocalDateTime.now();
        return new Servico(null, checklist, mecanico, agora, null, null, status, agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public Checklist getChecklist() { return checklist; }
    public Mecanico getMecanico() { return mecanico; }
    public LocalDateTime getDataAtribuicao() { return dataAtribuicao; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public StatusProcesso getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setChecklist(Checklist checklist) { this.checklist = checklist; }
    public void setMecanico(Mecanico mecanico) { this.mecanico = mecanico; }
    public void setDataAtribuicao(LocalDateTime dataAtribuicao) { this.dataAtribuicao = dataAtribuicao; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }
    public void setStatus(StatusProcesso status) { this.status = status; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    // Métodos de negócio
    public void servicoEmAndamento() {
        if (this.status != StatusProcesso.ATRIBUIDO) {
            throw new IllegalStateException(
                    "Apenas serviços atribuídos podem ser iniciados. Status atual: " + this.status);
        }
        if (this.mecanico == null) {
            throw new IllegalStateException("Não é possível iniciar um serviço sem mecânico atribuído.");
        }
        this.status = StatusProcesso.EM_ANDAMENTO;
        this.dataInicio = LocalDateTime.now();
        this.atualizadoEm = this.dataInicio;
    }

    public void atualizarStatus(StatusProcesso novoStatus) {
        this.status = novoStatus;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void concluir() {
        if (this.status != StatusProcesso.EM_ANDAMENTO) {
            throw new IllegalStateException("Apenas serviços em andamento podem ser concluídos.");
        }
        this.status = StatusProcesso.CONCLUIDO;
        this.dataConclusao = LocalDateTime.now();
        this.atualizadoEm = this.dataConclusao;
    }
}





