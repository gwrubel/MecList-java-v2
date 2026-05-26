package com.meclist.usecase.servico;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Checklist;
import com.meclist.domain.Servico;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.servico.ConcluirServicoResponse;
import com.meclist.exception.AcessoNegadoException;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.exception.ChecklistStatusInvalidoException;
import com.meclist.exception.ServicoNaoEncontradoException;
import com.meclist.exception.ServicoStatusInvalidoException;
import com.meclist.infra.ServicoConcluidoEvent;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ItemChecklistGateway;
import com.meclist.interfaces.OrcamentoGateway;
import com.meclist.interfaces.ServicoGateway;
import com.meclist.security.AuthenticatedUserProvider;

@Service
public class ConcluirServicoUseCase {

    private final ServicoGateway servicoGateway;
    private final ChecklistGateway checklistGateway;
    private final ItemChecklistGateway itemChecklistGateway;
    private final OrcamentoGateway orcamentoGateway;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public ConcluirServicoUseCase(ServicoGateway servicoGateway,
                                  ChecklistGateway checklistGateway,
                                  ItemChecklistGateway itemChecklistGateway,
                                  OrcamentoGateway orcamentoGateway,
                                  ApplicationEventPublisher eventPublisher,
                                  AuthenticatedUserProvider authenticatedUserProvider) {
        this.servicoGateway = servicoGateway;
        this.checklistGateway = checklistGateway;
        this.itemChecklistGateway = itemChecklistGateway;
        this.orcamentoGateway = orcamentoGateway;
        this.eventPublisher = eventPublisher;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @Transactional
    public ConcluirServicoResponse executar(Long servicoId) {
        var servico = servicoGateway.buscarPorId(servicoId)
                .orElseThrow(() -> new ServicoNaoEncontradoException(servicoId));

        validarMecanicoDono(servico);

        if (servico.getStatus() != StatusProcesso.EM_ANDAMENTO) {
            throw new ServicoStatusInvalidoException(
                    "Apenas serviços em andamento podem ser concluídos. Status atual: " + servico.getStatus());
        }

        Long checklistId = servico.getChecklist() != null ? servico.getChecklist().getId() : null;
        if (checklistId == null) {
            throw new ChecklistNaoEncontradoException("Checklist não encontrado para o serviço: " + servicoId);
        }

        var checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException("Checklist não encontrado: " + checklistId));

        if (checklist.getStatus() != StatusProcesso.EM_ANDAMENTO) {
            throw new ChecklistStatusInvalidoException(
                    "Checklist não está em andamento para conclusão. Status atual: " + checklist.getStatus());
        }

        var resultadoItens = checklist.processarItensConclusao();
        for (var item : resultadoItens.itensMarcados()) {
            itemChecklistGateway.salvar(item);
        }
        int itensMarcadosComoTrocaFeita = resultadoItens.itensMarcados().size();
        int itensSemAutorizacao = resultadoItens.itensSemAutorizacao();

        servico.concluir();
        checklist.concluir();

        orcamentoGateway.buscarPorChecklistId(checklist.getId()).ifPresent(orcamento -> {
            orcamento.concluir();
            orcamentoGateway.salvar(orcamento);
        });

        servicoGateway.salvar(servico);
        checklistGateway.salvar(checklist);

        eventPublisher.publishEvent(montarEvento(servico, checklist));

        return new ConcluirServicoResponse(
                servico.getId(),
                checklist.getId(),
                itensMarcadosComoTrocaFeita,
                itensSemAutorizacao,
                servico.getStatus(),
                checklist.getStatus());
    }

    private ServicoConcluidoEvent montarEvento(Servico servico, Checklist checklist) {
        var veiculo = checklist.getVeiculo();
        var cliente = veiculo != null ? veiculo.getCliente() : null;
        return new ServicoConcluidoEvent(
                servico.getId(),
                checklist.getId(),
                cliente != null ? cliente.getEmail() : null,
                cliente != null ? cliente.getNome() : null,
                veiculo != null ? veiculo.getPlaca() : null,
                veiculo != null ? veiculo.getMarca() : null,
                veiculo != null ? veiculo.getModelo() : null,
                LocalDateTime.now());
    }

    private void validarMecanicoDono(Servico servico) {
        var user = authenticatedUserProvider.get();
        if (!"MECANICO".equals(user.role())) {
            throw new AcessoNegadoException("Apenas mecânico pode concluir serviço.");
        }

        Long mecanicoDoServico = servico.getMecanico() != null ? servico.getMecanico().getId() : null;
        if (mecanicoDoServico == null || !mecanicoDoServico.equals(user.id())) {
            throw new AcessoNegadoException("Este serviço pertence a outro mecânico.");
        }
    }
}
