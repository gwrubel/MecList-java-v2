package com.meclist.persistence.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "id_status_item", nullable = false)
    private StatusItemEntity statusItem;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    @OneToMany(mappedBy = "itemChecklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoEvidenciaEntity> fotosEvidencia = new ArrayList<>();
    
    @OneToMany(mappedBy = "itemChecklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoEntity> produtos = new ArrayList<>();

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



