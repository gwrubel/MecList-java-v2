package com.meclist.usecase.checklist;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.meclist.domain.Checklist;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.checklist.ChecklistResumoResponse;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.mapper.ChecklistMapper;

@Service
public class ListarChecklistsPorStatusUseCase {

    private final ChecklistGateway checklistGateway;

    public ListarChecklistsPorStatusUseCase(ChecklistGateway checklistGateway) {
        this.checklistGateway = checklistGateway;
    }

    public List<ChecklistResumoResponse> executar(StatusProcesso status) {
        List<Checklist> checklists = checklistGateway.buscarPorStatus(status);

        return checklists.stream()
                .map(ChecklistMapper::toResumoResponse)
                .collect(Collectors.toList());
    }
}
