package com.meclist.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.meclist.domain.enums.StatusProcesso;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "servico")
public class ServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servico")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_checklist", nullable = false)
    private ChecklistEntity checklist;

    @ManyToOne
    @JoinColumn(name = "id_mecanico", nullable = false)
    private MecanicoEntity mecanico;

    @Column(name = "data_realizacao")
    private LocalDate dataRealizacao;

    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusProcesso status;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    


    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}