package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.dto.checklist.administrativo.RegistrarConfirmacaoManualRequest;
import com.meclist.interfaces.OrcamentoGateway;
import com.meclist.security.AuthenticatedUser;

@Service
public class RegistrarConfirmacaoManualUseCase {

    private final BuscarChecklistOrcamentoParaAprovacaoService buscarChecklistOrcamentoParaAprovacaoService;
    private final ChecklistWorkflowGuard workflowGuard;
    private final OrcamentoGateway orcamentoGateway;

    public RegistrarConfirmacaoManualUseCase(
            BuscarChecklistOrcamentoParaAprovacaoService buscarChecklistOrcamentoParaAprovacaoService,
            ChecklistWorkflowGuard workflowGuard,
            OrcamentoGateway orcamentoGateway) {
        this.buscarChecklistOrcamentoParaAprovacaoService = buscarChecklistOrcamentoParaAprovacaoService;
        this.workflowGuard = workflowGuard;
        this.orcamentoGateway = orcamentoGateway;
    }

    @Transactional
    public void executar(Long checklistId, RegistrarConfirmacaoManualRequest request) {
        ChecklistOrcamentoContext context = buscarChecklistOrcamentoParaAprovacaoService.carregar(checklistId);
        workflowGuard.validarRegistroConfirmacaoManualPorAdm(context.checklist(), context.orcamento());
        AuthenticatedUser user = workflowGuard.obterUsuarioAutenticado();

        context.orcamento().registrarConfirmacaoManual(
                user.id(),
                request.canalConfirmacao(),
                request.observacao());

        orcamentoGateway.salvar(context.orcamento());
    }
}