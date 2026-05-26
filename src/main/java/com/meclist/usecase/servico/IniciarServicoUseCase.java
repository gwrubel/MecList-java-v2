package com.meclist.usecase.servico;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Servico;
import com.meclist.dto.servico.IniciarServicoResponse;
import com.meclist.exception.AcessoNegadoException;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.exception.ServicoNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ServicoGateway;
import com.meclist.security.AuthenticatedUserProvider;

@Service
public class IniciarServicoUseCase {

    private final ServicoGateway servicoGateway;
    private final ChecklistGateway checklistGateway;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public IniciarServicoUseCase(ServicoGateway servicoGateway,
                                 ChecklistGateway checklistGateway,
                                 AuthenticatedUserProvider authenticatedUserProvider) {
        this.servicoGateway = servicoGateway;
        this.checklistGateway = checklistGateway;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @Transactional
    public IniciarServicoResponse executar(Long servicoId) {
        var servico = servicoGateway.buscarPorId(servicoId)
                .orElseThrow(() -> new ServicoNaoEncontradoException(servicoId));

        validarMecanicoDono(servico);

        Long checklistId = servico.getChecklist() != null ? servico.getChecklist().getId() : null;
        if (checklistId == null) {
            throw new ChecklistNaoEncontradoException("Checklist não encontrado para o serviço: " + servicoId);
        }

        var checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException("Checklist não encontrado: " + checklistId));

        servico.servicoEmAndamento();
        checklist.emAndamento();

        servicoGateway.salvar(servico);
        checklistGateway.salvar(checklist);

        return new IniciarServicoResponse(
                servico.getId(),
                checklist.getId(),
                servico.getStatus(),
                checklist.getStatus(),
                servico.getDataInicio());
    }

    private void validarMecanicoDono(Servico servico) {
        var user = authenticatedUserProvider.get();
        if (!"MECANICO".equals(user.role())) {
            throw new AcessoNegadoException("Apenas mecânico pode iniciar serviço.");
        }

        Long mecanicoDoServico = servico.getMecanico() != null ? servico.getMecanico().getId() : null;
        if (mecanicoDoServico == null || !mecanicoDoServico.equals(user.id())) {
            throw new AcessoNegadoException("Este serviço pertence a outro mecânico.");
        }
    }
}
