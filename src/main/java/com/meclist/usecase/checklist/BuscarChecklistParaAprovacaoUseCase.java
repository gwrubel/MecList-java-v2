package com.meclist.usecase.checklist;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.meclist.domain.Checklist;
import com.meclist.domain.Orcamento;
import com.meclist.dto.checklist.aprovacao.ChecklistAprovacaoResponse;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.OrcamentoGateway;
import com.meclist.mapper.ChecklistMapper;

@Service
public class BuscarChecklistParaAprovacaoUseCase {

    private final ChecklistGateway checklistGateway;
    private final OrcamentoGateway orcamentoGateway;
    private final ChecklistWorkflowGuard workflowGuard;

    public BuscarChecklistParaAprovacaoUseCase(ChecklistGateway checklistGateway,
                                                OrcamentoGateway orcamentoGateway,
                                                ChecklistWorkflowGuard workflowGuard) {
        this.checklistGateway = checklistGateway;
        this.orcamentoGateway = orcamentoGateway;
        this.workflowGuard = workflowGuard;
    }

    public ChecklistAprovacaoResponse executar(Long checklistId) {
        Checklist checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException(
                        "Checklist não encontrado: " + checklistId));

        workflowGuard.validarAprovacaoPorCliente(checklist);

        // Busca o valor total do orçamento
        BigDecimal valorTotal = orcamentoGateway.buscarPorChecklistId(checklistId)
                .map(Orcamento::getValorTotal)
                .orElse(BigDecimal.ZERO);

        return ChecklistMapper.toAprovacaoResponse(checklist, valorTotal);
    }
}
