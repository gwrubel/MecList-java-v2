package com.meclist.usecase.checklist;

import com.meclist.domain.Checklist;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.enums.StatusItem;
import com.meclist.dto.checklist.precificacao.ChecklistPrecificacaoResponse;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.mapper.ChecklistMapper;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BuscarChecklistParaPrecificacaoUseCase {

    private final ChecklistGateway checklistGateway;

    public BuscarChecklistParaPrecificacaoUseCase(ChecklistGateway checklistGateway) {
        this.checklistGateway = checklistGateway;
    }

    public ChecklistPrecificacaoResponse executar(Long checklistId) {
        Checklist checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException(
                        "Checklist não encontrado: " + checklistId));

        // Filtra apenas itens que precisam de precificação
        List<ItemChecklist> itensParaPrecificar = checklist.getItensChecklist().stream()
                .filter(item -> item.getStatusItem() == StatusItem.TROCAR)
                .toList();

        return ChecklistMapper.toPrecificacaoResponse(checklist, itensParaPrecificar);
    }
}
