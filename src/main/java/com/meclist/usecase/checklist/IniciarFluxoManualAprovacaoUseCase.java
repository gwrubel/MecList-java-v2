package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.enums.EtapaFluxoManual;
import com.meclist.exception.ChecklistStatusInvalidoException;
import com.meclist.interfaces.OrcamentoGateway;

@Service
public class IniciarFluxoManualAprovacaoUseCase {

    private final BuscarChecklistOrcamentoParaAprovacaoService buscarChecklistOrcamentoParaAprovacaoService;
    private final ChecklistWorkflowGuard workflowGuard;
    private final OrcamentoGateway orcamentoGateway;

    public IniciarFluxoManualAprovacaoUseCase(
            BuscarChecklistOrcamentoParaAprovacaoService buscarChecklistOrcamentoParaAprovacaoService,
            ChecklistWorkflowGuard workflowGuard,
            OrcamentoGateway orcamentoGateway) {
        this.buscarChecklistOrcamentoParaAprovacaoService = buscarChecklistOrcamentoParaAprovacaoService;
        this.workflowGuard = workflowGuard;
        this.orcamentoGateway = orcamentoGateway;
    }

    @Transactional
    public void executar(Long checklistId) {
        ChecklistOrcamentoContext context = buscarChecklistOrcamentoParaAprovacaoService.carregar(checklistId);
        workflowGuard.validarInicioFluxoManualPorAdm(context.checklist());

        if (context.orcamento().getEtapaFluxoManual() != null
                && context.orcamento().getEtapaFluxoManual() != EtapaFluxoManual.NAO_INICIADO) {
            throw new ChecklistStatusInvalidoException("O fluxo manual já foi iniciado para este checklist.");
        }

        context.orcamento().iniciarFluxoManual();
        orcamentoGateway.salvar(context.orcamento());
    }
}