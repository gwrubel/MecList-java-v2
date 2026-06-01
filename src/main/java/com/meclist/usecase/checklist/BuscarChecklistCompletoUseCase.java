package com.meclist.usecase.checklist;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.checklist.visualizacao.ChecklistVisualizacaoCompletaResponse;
import com.meclist.exception.AcessoNegadoException;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.exception.ChecklistStatusInvalidoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.OrcamentoGateway;
import com.meclist.interfaces.ServicoGateway;
import com.meclist.mapper.ChecklistMapper;
import com.meclist.security.AuthenticatedUserProvider;

@Service
public class BuscarChecklistCompletoUseCase {

    private static final Set<StatusProcesso> STATUSES_PERMITIDOS = Set.of(
            StatusProcesso.EM_ANDAMENTO,
            StatusProcesso.CONCLUIDO
    );

    private final ChecklistGateway checklistGateway;
    private final OrcamentoGateway orcamentoGateway;
    private final ServicoGateway servicoGateway;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public BuscarChecklistCompletoUseCase(ChecklistGateway checklistGateway,
                                          OrcamentoGateway orcamentoGateway,
                                          ServicoGateway servicoGateway,
                                          AuthenticatedUserProvider authenticatedUserProvider) {
        this.checklistGateway = checklistGateway;
        this.orcamentoGateway = orcamentoGateway;
        this.servicoGateway = servicoGateway;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    public ChecklistVisualizacaoCompletaResponse executar(Long checklistId) {
        var checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException("Checklist nao encontrado: " + checklistId));

        validarStatus(checklist.getStatus());
        validarAcesso(checklist);

        var orcamento = orcamentoGateway.buscarPorChecklistId(checklistId).orElse(null);

        var servicoExecutor = servicoGateway
            .buscarExecutorPorChecklistId(checklistId, checklist.getStatus())
            .orElse(null);

        String nomeMecanicoExecutor = servicoExecutor != null && servicoExecutor.getMecanico() != null
            ? servicoExecutor.getMecanico().getNome()
            : null;

        LocalDateTime dataConclusao = servicoExecutor != null
            ? servicoExecutor.getDataConclusao()
            : null;

        return ChecklistMapper.toVisualizacaoCompletaResponse(
            checklist,
            orcamento,
            nomeMecanicoExecutor,
            dataConclusao);
    }

    private void validarStatus(StatusProcesso status) {
        if (!STATUSES_PERMITIDOS.contains(status)) {
            throw new ChecklistStatusInvalidoException(
                    "Visualizacao completa disponivel apenas apos precificacao ate concluido. Status atual: " + status);
        }
    }

    private void validarAcesso(com.meclist.domain.Checklist checklist) {
        var user = authenticatedUserProvider.get();

        if ("ADMIN".equals(user.role()) || "ADM".equals(user.role())) {
            return;
        }

        if ("MECANICO".equals(user.role())) {
            Long mecanicoChecklist = checklist.getMecanico() != null ? checklist.getMecanico().getId() : null;
            if (mecanicoChecklist != null && mecanicoChecklist.equals(user.id())) {
                return;
            }
            throw new AcessoNegadoException("Este checklist pertence a outro mecanico.");
        }

        if ("CLIENTE".equals(user.role())) {
            Long clienteChecklist = checklist.getVeiculo() != null && checklist.getVeiculo().getCliente() != null
                    ? checklist.getVeiculo().getCliente().getId()
                    : null;
            if (clienteChecklist != null && clienteChecklist.equals(user.id())) {
                return;
            }
            throw new AcessoNegadoException("Este checklist pertence a outro cliente.");
        }

        throw new AcessoNegadoException("Voce nao tem permissao para visualizar este checklist.");
    }
}
