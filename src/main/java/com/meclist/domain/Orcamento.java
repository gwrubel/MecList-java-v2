package com.meclist.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.meclist.domain.enums.CanalConfirmacaoCliente;
import com.meclist.domain.enums.EtapaFluxoManual;
import com.meclist.domain.enums.OrigemAprovacao;
import com.meclist.domain.enums.StatusProcesso;

public class Orcamento {
    private Long id;
    private Checklist checklist;
    private BigDecimal valorTotal;
    private LocalDate dataEmissao;
    private LocalDate dataAprovacao;
    private StatusProcesso status;
    private EtapaFluxoManual etapaFluxoManual;
    private LocalDateTime ultimoPdfGeradoEm;
    private Long ultimoPdfGeradoPorAdmId;
    private LocalDateTime confirmacaoManualEm;
    private Long confirmacaoManualPorAdmId;
    private CanalConfirmacaoCliente canalConfirmacaoCliente;
    private String observacaoConfirmacao;
    private OrigemAprovacao origemAprovacaoFinal;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Orcamento(Long id, Checklist checklist, BigDecimal valorTotal, LocalDate dataEmissao,
                     LocalDate dataAprovacao, StatusProcesso status,
                     LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this(id, checklist, valorTotal, dataEmissao, dataAprovacao, status,
                EtapaFluxoManual.NAO_INICIADO, null, null, null, null, null, null, null,
                criadoEm, atualizadoEm);
    }

    public Orcamento(Long id, Checklist checklist, BigDecimal valorTotal, LocalDate dataEmissao,
                     LocalDate dataAprovacao, StatusProcesso status, EtapaFluxoManual etapaFluxoManual,
                     LocalDateTime ultimoPdfGeradoEm, Long ultimoPdfGeradoPorAdmId,
                     LocalDateTime confirmacaoManualEm, Long confirmacaoManualPorAdmId,
                     CanalConfirmacaoCliente canalConfirmacaoCliente, String observacaoConfirmacao,
                     OrigemAprovacao origemAprovacaoFinal, LocalDateTime criadoEm,
                     LocalDateTime atualizadoEm) {
        this.id = id;
        this.checklist = checklist;
        this.valorTotal = valorTotal;
        this.dataEmissao = dataEmissao;
        this.dataAprovacao = dataAprovacao;
        this.status = status;
        this.etapaFluxoManual = etapaFluxoManual;
        this.ultimoPdfGeradoEm = ultimoPdfGeradoEm;
        this.ultimoPdfGeradoPorAdmId = ultimoPdfGeradoPorAdmId;
        this.confirmacaoManualEm = confirmacaoManualEm;
        this.confirmacaoManualPorAdmId = confirmacaoManualPorAdmId;
        this.canalConfirmacaoCliente = canalConfirmacaoCliente;
        this.observacaoConfirmacao = observacaoConfirmacao;
        this.origemAprovacaoFinal = origemAprovacaoFinal;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Orcamento novo(Checklist checklist, BigDecimal valorTotal, StatusProcesso status) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDate hoje = LocalDate.now();
        return new Orcamento(null, checklist, valorTotal, hoje, null, status, agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public Checklist getChecklist() { return checklist; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public LocalDate getDataEmissao() { return dataEmissao; }
    public LocalDate getDataAprovacao() { return dataAprovacao; }
    public StatusProcesso getStatus() { return status; }
    public EtapaFluxoManual getEtapaFluxoManual() { return etapaFluxoManual; }
    public LocalDateTime getUltimoPdfGeradoEm() { return ultimoPdfGeradoEm; }
    public Long getUltimoPdfGeradoPorAdmId() { return ultimoPdfGeradoPorAdmId; }
    public LocalDateTime getConfirmacaoManualEm() { return confirmacaoManualEm; }
    public Long getConfirmacaoManualPorAdmId() { return confirmacaoManualPorAdmId; }
    public CanalConfirmacaoCliente getCanalConfirmacaoCliente() { return canalConfirmacaoCliente; }
    public String getObservacaoConfirmacao() { return observacaoConfirmacao; }
    public OrigemAprovacao getOrigemAprovacaoFinal() { return origemAprovacaoFinal; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setChecklist(Checklist checklist) { this.checklist = checklist; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }
    public void setDataAprovacao(LocalDate dataAprovacao) { this.dataAprovacao = dataAprovacao; }
    public void setStatus(StatusProcesso status) { this.status = status; }
    public void setEtapaFluxoManual(EtapaFluxoManual etapaFluxoManual) { this.etapaFluxoManual = etapaFluxoManual; }
    public void setUltimoPdfGeradoEm(LocalDateTime ultimoPdfGeradoEm) { this.ultimoPdfGeradoEm = ultimoPdfGeradoEm; }
    public void setUltimoPdfGeradoPorAdmId(Long ultimoPdfGeradoPorAdmId) { this.ultimoPdfGeradoPorAdmId = ultimoPdfGeradoPorAdmId; }
    public void setConfirmacaoManualEm(LocalDateTime confirmacaoManualEm) { this.confirmacaoManualEm = confirmacaoManualEm; }
    public void setConfirmacaoManualPorAdmId(Long confirmacaoManualPorAdmId) { this.confirmacaoManualPorAdmId = confirmacaoManualPorAdmId; }
    public void setCanalConfirmacaoCliente(CanalConfirmacaoCliente canalConfirmacaoCliente) { this.canalConfirmacaoCliente = canalConfirmacaoCliente; }
    public void setObservacaoConfirmacao(String observacaoConfirmacao) { this.observacaoConfirmacao = observacaoConfirmacao; }
    public void setOrigemAprovacaoFinal(OrigemAprovacao origemAprovacaoFinal) { this.origemAprovacaoFinal = origemAprovacaoFinal; }
   
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    // Métodos de negócio
  

    public void aprovar() {
        this.dataAprovacao = LocalDate.now();
        this.status = StatusProcesso.APROVADO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void rejeitar() {
        this.status = StatusProcesso.REPROVADO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void iniciarFluxoManual() {
        this.etapaFluxoManual = EtapaFluxoManual.INICIADO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void registrarGeracaoPdf(Long adminId) {
        this.ultimoPdfGeradoPorAdmId = adminId;
        this.ultimoPdfGeradoEm = LocalDateTime.now();
        this.etapaFluxoManual = EtapaFluxoManual.PDF_GERADO;
        this.atualizadoEm = this.ultimoPdfGeradoEm;
    }

    public void registrarConfirmacaoManual(Long adminId,
                                           CanalConfirmacaoCliente canalConfirmacaoCliente,
                                           String observacaoConfirmacao) {
        this.confirmacaoManualPorAdmId = adminId;
        this.confirmacaoManualEm = LocalDateTime.now();
        this.canalConfirmacaoCliente = canalConfirmacaoCliente;
        this.observacaoConfirmacao = observacaoConfirmacao;
        this.etapaFluxoManual = EtapaFluxoManual.CONFIRMACAO_REGISTRADA;
        this.atualizadoEm = this.confirmacaoManualEm;
    }

    public void finalizarFluxoManual(OrigemAprovacao origemAprovacaoFinal) {
        this.origemAprovacaoFinal = origemAprovacaoFinal;
        this.etapaFluxoManual = EtapaFluxoManual.CONCLUIDO;
        this.atualizadoEm = LocalDateTime.now();
    }

   
}





