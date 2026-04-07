package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Checklist;
import com.meclist.domain.enums.StatusItem;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;

@Service
public class EnviarChecklistParaPrecificacaoUseCase {

    private final ChecklistGateway checklistGateway;
    private final ChecklistWorkflowGuard workflowGuard;

    public EnviarChecklistParaPrecificacaoUseCase(
            ChecklistGateway checklistGateway,
            ChecklistWorkflowGuard workflowGuard) {
        this.checklistGateway = checklistGateway;
        this.workflowGuard = workflowGuard;
    }

    @Transactional
    public void executar(Long checklistId) {
        Checklist checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException(
                        "Checklist não encontrado: " + checklistId));

        workflowGuard.validarEnvioParaPrecificacaoPorMecanico(checklist);

        boolean existePendente = checklist.getItensChecklist().stream()
                .anyMatch(item -> item.getStatusItem() == StatusItem.PENDENTE);

        if (existePendente) {
            throw new IllegalArgumentException(
                    "Não é possível enviar para precificação com itens pendentes.");
        }

        checklist.atualizarStatus(StatusProcesso.AGUARDANDO_PRECIFICACAO);
        checklistGateway.atualizarStatus(checklist);
    }
}