package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.enums.OrigemAprovacao;
import com.meclist.dto.checklist.aprovacao.AprovarChecklistRequest;
import com.meclist.security.AuthenticatedUser;

@Service
public class AprovarChecklistAdministrativoUseCase {

    private final BuscarChecklistOrcamentoParaAprovacaoService buscarChecklistOrcamentoParaAprovacaoService;
    private final ChecklistWorkflowGuard workflowGuard;
    private final ProcessarAprovacaoChecklistService processarAprovacaoChecklistService;

    public AprovarChecklistAdministrativoUseCase(
            BuscarChecklistOrcamentoParaAprovacaoService buscarChecklistOrcamentoParaAprovacaoService,
            ChecklistWorkflowGuard workflowGuard,
            ProcessarAprovacaoChecklistService processarAprovacaoChecklistService) {
        this.buscarChecklistOrcamentoParaAprovacaoService = buscarChecklistOrcamentoParaAprovacaoService;
        this.workflowGuard = workflowGuard;
        this.processarAprovacaoChecklistService = processarAprovacaoChecklistService;
    }

    @Transactional
    public void executar(Long checklistId, AprovarChecklistRequest request) {
        ChecklistOrcamentoContext context = buscarChecklistOrcamentoParaAprovacaoService.carregar(checklistId);
        workflowGuard.validarAprovacaoManualPorAdm(context.checklist(), context.orcamento());
        AuthenticatedUser user = workflowGuard.obterUsuarioAutenticado();

        processarAprovacaoChecklistService.processar(
                context.checklist(),
                context.orcamento(),
                request,
                OrigemAprovacao.ADMIN_MANUAL,
                user.id(),
                user.role());
    }
}