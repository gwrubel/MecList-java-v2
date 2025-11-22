package com.meclist.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "checklist_produto")
public class ChecklistProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_checklist_produto")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_item_checklist", nullable = false)
    private ItemChecklistEntity itemChecklist;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private ProdutoEntity produto;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "valor_uni", precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "aprovado_cliente")
    private Boolean aprovadoCliente; // NULL = Pendente, TRUE = Aprovado, FALSE = Rejeitado

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

