package com.meclist.persistence.entity;

import java.time.LocalDateTime;
import java.util.Date;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orcamento")
public class OrcamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orcamento")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_checklist", nullable = false)
    private ChecklistEntity checklist;

    @Column(name = "valor_total")
    private Float valorTotal;

    @Column(name = "data_emissao")
    private Date dataEmissao;

    @Column(name = "data_aprovacao")
    private Date dataAprovacao;

    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false)
    private StatusEntity status;

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