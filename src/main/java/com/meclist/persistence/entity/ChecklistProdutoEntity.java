package com.meclist.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.meclist.domain.enums.OrigemAprovacao;

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

    @Column(name = "nome_produto_snapshot")
    private String nomeProdutoSnapshot; // Para armazenar o nome do produto no momento do orçamento, caso o produto seja alterado posteriormente

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "valor_uni", precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "marca")
    private String marca;

    @Column(name = "aprovado_cliente")
    private Boolean aprovadoCliente; // NULL = Pendente, TRUE = Aprovado, FALSE = Rejeitado

    @Enumerated(EnumType.STRING)
    @Column(name = "origem_decisao")
    private OrigemAprovacao origemDecisao;

    @Column(name = "decidido_por_id")
    private Long decididoPorId;

    @Column(name = "decidido_por_tipo")
    private String decididoPorTipo;

    @Column(name = "decidido_em")
    private LocalDateTime decididoEm;

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

