package com.meclist.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.meclist.domain.enums.StatusItem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "item_checklist")
public class ItemChecklistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_checklist")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_checklist", nullable = false)
    private ChecklistEntity checklist;

    @ManyToOne
    @JoinColumn(name = "id_item", nullable = false)
    private ItemEntity item;

    @Column(name = "nome_item_snapshot")
    private String nomeItemSnapshot; // Para armazenar o nome do item no momento do checklist, caso o item seja alterado posteriormente

    @Column(name = "mao_de_obra", precision = 10, scale = 2)
    private BigDecimal maoDeObra;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_item", nullable = false)
    private StatusItem statusItem;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    @OneToMany(mappedBy = "itemChecklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoEvidenciaEntity> fotosEvidencia = new ArrayList<>();
    
    @OneToMany(mappedBy = "itemChecklist")
    private List<ChecklistProdutoEntity> produtosOrcados = new ArrayList<>();

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



