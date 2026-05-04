package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Checklist;
import com.meclist.domain.Orcamento;
import com.meclist.domain.enums.OrigemAprovacao;
import com.meclist.dto.checklist.aprovacao.AprovarChecklistRequest;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.OrcamentoGateway;
import com.meclist.security.AuthenticatedUser;

@Service
public class AprovarChecklistUseCase {

    private final ChecklistGateway checklistGateway;
    private final OrcamentoGateway orcamentoGateway;
    private final ChecklistWorkflowGuard workflowGuard;
    private final ProcessarAprovacaoChecklistService processarAprovacaoChecklistService;

    public AprovarChecklistUseCase(ChecklistGateway checklistGateway,
                                   OrcamentoGateway orcamentoGateway,
                                   ChecklistWorkflowGuard workflowGuard,
                                   ProcessarAprovacaoChecklistService processarAprovacaoChecklistService) {
        this.checklistGateway = checklistGateway;
        this.orcamentoGateway = orcamentoGateway;
        this.workflowGuard = workflowGuard;
        this.processarAprovacaoChecklistService = processarAprovacaoChecklistService;
    }

    @Transactional
    public void executar(Long checklistId, AprovarChecklistRequest request) {
        Checklist checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException(
                        "Checklist não encontrado: " + checklistId));

        workflowGuard.validarAprovacaoPorCliente(checklist);
        AuthenticatedUser user = workflowGuard.obterUsuarioAutenticado();

        Orcamento orcamento = orcamentoGateway.buscarPorChecklistId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException(
                        "Orçamento não encontrado para o checklist: " + checklistId));

        processarAprovacaoChecklistService.processar(
                checklist,
                orcamento,
                request,
                OrigemAprovacao.CLIENTE_PORTAL,
                user.id(),
                user.role());
    }
}
