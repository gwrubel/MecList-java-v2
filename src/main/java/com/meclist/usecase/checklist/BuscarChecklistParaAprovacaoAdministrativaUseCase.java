package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;

import com.meclist.dto.checklist.aprovacao.ChecklistAprovacaoResponse;
import com.meclist.mapper.ChecklistMapper;

@Service
public class BuscarChecklistParaAprovacaoAdministrativaUseCase {

    private final BuscarChecklistOrcamentoParaAprovacaoService buscarChecklistOrcamentoParaAprovacaoService;
    private final ChecklistWorkflowGuard workflowGuard;

    public BuscarChecklistParaAprovacaoAdministrativaUseCase(
            BuscarChecklistOrcamentoParaAprovacaoService buscarChecklistOrcamentoParaAprovacaoService,
            ChecklistWorkflowGuard workflowGuard) {
        this.buscarChecklistOrcamentoParaAprovacaoService = buscarChecklistOrcamentoParaAprovacaoService;
        this.workflowGuard = workflowGuard;
    }

    public ChecklistAprovacaoResponse executar(Long checklistId) {
        ChecklistOrcamentoContext context = buscarChecklistOrcamentoParaAprovacaoService.carregar(checklistId);
        workflowGuard.validarInicioFluxoManualPorAdm(context.checklist());

        return ChecklistMapper.toAprovacaoResponse(
                context.checklist(),
                context.orcamento().getValorTotal(),
                context.orcamento().getEtapaFluxoManual());
    }
}