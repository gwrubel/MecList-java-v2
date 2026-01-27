package com.meclist.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.meclist.domain.enums.CategoriaParteVeiculo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Long id;

    @Column(name = "nome_item", nullable = false)
    private String nomeItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "parte_do_veiculo", nullable = false)
    private CategoriaParteVeiculo parteDoVeiculo;

    @Column(name = "imagem_ilustrativa")
    private String imagemIlustrativa;

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
