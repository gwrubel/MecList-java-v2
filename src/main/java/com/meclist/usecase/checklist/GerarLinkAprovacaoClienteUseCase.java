package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;

import com.meclist.domain.TokenAprovacaoChecklist;
import com.meclist.dto.checklist.aprovacaoLink.GerarLinkAprovacaoResponse;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.exception.ClienteNaoEncontradoException;
import com.meclist.infra.EmailService;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.TokenAprovacaoChecklistGateway;

@Service
public class GerarLinkAprovacaoClienteUseCase {

    private static final int HORAS_VALIDADE_LINK = 24;

    private final ChecklistGateway checklistGateway;
    private final TokenAprovacaoChecklistGateway tokenAprovacaoChecklistGateway;
    private final ChecklistWorkflowGuard workflowGuard;
    private final EmailService emailService;

    public GerarLinkAprovacaoClienteUseCase(ChecklistGateway checklistGateway,
                                            TokenAprovacaoChecklistGateway tokenAprovacaoChecklistGateway,
                                            ChecklistWorkflowGuard workflowGuard,
                                            EmailService emailService) {
        this.checklistGateway = checklistGateway;
        this.tokenAprovacaoChecklistGateway = tokenAprovacaoChecklistGateway;
        this.workflowGuard = workflowGuard;
        this.emailService = emailService;
    }

    public GerarLinkAprovacaoResponse executar(Long checklistId) {
        var checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException("Checklist não encontrado: " + checklistId));

        workflowGuard.validarInicioFluxoManualPorAdm(checklist);

        var cliente = checklist.getVeiculo() != null ? checklist.getVeiculo().getCliente() : null;
        if (cliente == null || cliente.getId() == null || cliente.getEmail() == null) {
            throw new ClienteNaoEncontradoException("Cliente não encontrado para o checklist: " + checklistId);
        }

        TokenAprovacaoChecklist token = TokenAprovacaoChecklist.gerar(cliente.getId(), checklistId, HORAS_VALIDADE_LINK);
        TokenAprovacaoChecklist tokenSalvo = tokenAprovacaoChecklistGateway.salvar(token);

        emailService.enviarEmailLinkAprovacaoChecklist(cliente.getEmail(), checklistId, tokenSalvo.getToken(), HORAS_VALIDADE_LINK);
        String link = emailService.montarLinkAprovacaoChecklist(checklistId, tokenSalvo.getToken());

        return new GerarLinkAprovacaoResponse(checklistId, link, tokenSalvo.getExpiraEm());
    }
}