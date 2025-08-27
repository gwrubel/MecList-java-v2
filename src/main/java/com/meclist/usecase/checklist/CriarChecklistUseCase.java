package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Checklist;
import com.meclist.dto.checklist.CriarChecklistRequest;
import com.meclist.interfaces.ChecklistGateway;

@Service
public class CriarChecklistUseCase {

    private final ChecklistGateway checklistGateway;

    public CriarChecklistUseCase(ChecklistGateway checklistGateway) {
        this.checklistGateway = checklistGateway;
    }

    @Transactional
    public Checklist executar(CriarChecklistRequest req) {
        var checklist = Checklist.novo(req.idVeiculo(), req.idMecanico(), req.quilometragem(), req.descricao(), req.idStatus());
        return checklistGateway.salvar(checklist);
    }
}



