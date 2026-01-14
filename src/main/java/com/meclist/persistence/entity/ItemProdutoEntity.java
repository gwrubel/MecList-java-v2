package com.meclist.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_produto", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_item", "id_produto"})
})
public class ItemProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_produto")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_item", nullable = false)
    private ItemEntity item;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private ProdutoEntity produto;

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

