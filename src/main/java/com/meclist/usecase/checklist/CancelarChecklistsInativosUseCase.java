package com.meclist.usecase.checklist;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Checklist;
import com.meclist.interfaces.ChecklistGateway;

@Service
public class CancelarChecklistsInativosUseCase {

    private static final int DIAS_INATIVIDADE = 7;

    private final ChecklistGateway checklistGateway;

    public CancelarChecklistsInativosUseCase(ChecklistGateway checklistGateway) {
        this.checklistGateway = checklistGateway;
    }

    @Transactional
    public int executar() {
        LocalDateTime limite = LocalDateTime.now().minusDays(DIAS_INATIVIDADE);
        List<Checklist> inativos = checklistGateway.buscarInativosParaCancelamento(limite);

        for (Checklist checklist : inativos) {
            checklist.cancelar();
            checklistGateway.atualizarStatus(checklist);
        }

        return inativos.size();
    }
}
