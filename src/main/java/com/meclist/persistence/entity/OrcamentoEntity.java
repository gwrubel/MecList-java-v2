package com.meclist.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.meclist.domain.enums.CanalConfirmacaoCliente;
import com.meclist.domain.enums.EtapaFluxoManual;
import com.meclist.domain.enums.OrigemAprovacao;
import com.meclist.domain.enums.StatusProcesso;

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

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "data_emissao")
    private LocalDateTime dataEmissao;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusProcesso status;

    @Enumerated(EnumType.STRING)
    @Column(name = "etapa_fluxo_manual")
    private EtapaFluxoManual etapaFluxoManual;

    @Column(name = "ultimo_pdf_gerado_em")
    private LocalDateTime ultimoPdfGeradoEm;

    @Column(name = "ultimo_pdf_gerado_por_adm_id")
    private Long ultimoPdfGeradoPorAdmId;

    @Column(name = "confirmacao_manual_em")
    private LocalDateTime confirmacaoManualEm;

    @Column(name = "confirmacao_manual_por_adm_id")
    private Long confirmacaoManualPorAdmId;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal_confirmacao_cliente")
    private CanalConfirmacaoCliente canalConfirmacaoCliente;

    @Column(name = "observacao_confirmacao", length = 1000)
    private String observacaoConfirmacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "origem_aprovacao_final")
    private OrigemAprovacao origemAprovacaoFinal;

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