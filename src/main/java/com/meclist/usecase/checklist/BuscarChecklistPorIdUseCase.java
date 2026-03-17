package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;

import com.meclist.dto.checklist.ChecklistResponse;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.mapper.ChecklistMapper;

@Service
public class BuscarChecklistPorIdUseCase {

    private final ChecklistGateway checklistGateway;

    public BuscarChecklistPorIdUseCase(ChecklistGateway checklistGateway) {
        this.checklistGateway = checklistGateway;
    }

    public ChecklistResponse executar(Long checklistId) {
        var checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() ->
                        new ChecklistNaoEncontradoException("Checklist não encontrado!"));

        return ChecklistMapper.toResponse(checklist);
    }
}
