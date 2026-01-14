package com.meclist.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.meclist.domain.enums.Situacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Mecanicos")
public class MecanicoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    private String senha;

    @Column(name = "tipo_de_usuario")
    private String tipoDeUsuario;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    @OneToMany(mappedBy = "mecanico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistEntity> checklists;
    
    @OneToMany(mappedBy = "mecanico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicoEntity> servicos;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;

    private String telefone;

    @Column(unique = true, nullable = false)
    private String cpf;

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
