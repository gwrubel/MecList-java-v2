package com.meclist.usecase.servico;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Servico;
import com.meclist.domain.enums.Situacao;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.servico.VincularServicoRequest;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.exception.ChecklistStatusInvalidoException;
import com.meclist.exception.MecanicoNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.interfaces.ServicoGateway;

@Service
public class VincularChecklistAoServicoUseCase {

    private final ChecklistGateway checklistGateway;
    private final MecanicoGateway mecanicoGateway;
    private final ServicoGateway servicoGateway;

    public VincularChecklistAoServicoUseCase(ChecklistGateway checklistGateway,
                                             MecanicoGateway mecanicoGateway,
                                             ServicoGateway servicoGateway) {
        this.checklistGateway = checklistGateway;
        this.mecanicoGateway = mecanicoGateway;
        this.servicoGateway = servicoGateway;
    }

    @Transactional
    public void executar(Long checklistId, VincularServicoRequest request) {
        var checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException(
                        "Checklist não encontrado: " + checklistId));

        if (checklist.getStatus() != StatusProcesso.APROVADO) {
            throw new ChecklistStatusInvalidoException(
                    "Apenas checklists aprovados podem ser vinculados a um serviço.");
        }

        var mecanico = mecanicoGateway.bucarPorId(request.mecanicoId())
                .orElseThrow(() -> new MecanicoNaoEncontradoException(request.mecanicoId()));

        if (mecanico.getSituacao() != Situacao.ATIVO) {
            throw new IllegalArgumentException("O mecânico informado está inativo.");
        }

        Servico servico = Servico.novo(checklist, mecanico, null, StatusProcesso.EM_ANDAMENTO);
        checklist.emAndamento();

        servicoGateway.salvar(servico);
        checklistGateway.salvar(checklist);
    }
}
