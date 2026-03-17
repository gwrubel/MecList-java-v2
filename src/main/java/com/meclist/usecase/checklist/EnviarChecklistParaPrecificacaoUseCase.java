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

    public EnviarChecklistParaPrecificacaoUseCase(ChecklistGateway checklistGateway) {
        this.checklistGateway = checklistGateway;
    }

    @Transactional
    public void executar(Long checklistId) {
        Checklist checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException(
                        "Checklist não encontrado: " + checklistId));

        if (checklist.getStatus() != StatusProcesso.EM_ANDAMENTO) {
            throw new IllegalArgumentException(
                    "Checklist só pode ser enviado para precificação quando estiver EM_ANDAMENTO.");
        }

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