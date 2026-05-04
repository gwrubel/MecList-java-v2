package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;

import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.OrcamentoGateway;

@Service
public class BuscarChecklistOrcamentoParaAprovacaoService {

    private final ChecklistGateway checklistGateway;
    private final OrcamentoGateway orcamentoGateway;

    public BuscarChecklistOrcamentoParaAprovacaoService(ChecklistGateway checklistGateway,
                                                       OrcamentoGateway orcamentoGateway) {
        this.checklistGateway = checklistGateway;
        this.orcamentoGateway = orcamentoGateway;
    }

    public ChecklistOrcamentoContext carregar(Long checklistId) {
        var checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException(
                        "Checklist não encontrado: " + checklistId));

        var orcamento = orcamentoGateway.buscarPorChecklistId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException(
                        "Orçamento não encontrado para o checklist: " + checklistId));

        return new ChecklistOrcamentoContext(checklist, orcamento);
    }
}