package com.meclist.usecase.servico;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.enums.StatusItem;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.servico.ConcluirServicoResponse;
import com.meclist.exception.AcessoNegadoException;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.exception.ChecklistStatusInvalidoException;
import com.meclist.exception.ServicoNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ItemChecklistGateway;
import com.meclist.interfaces.ServicoGateway;
import com.meclist.security.AuthenticatedUserProvider;

@Service
public class ConcluirServicoUseCase {

    private final ServicoGateway servicoGateway;
    private final ChecklistGateway checklistGateway;
    private final ItemChecklistGateway itemChecklistGateway;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public ConcluirServicoUseCase(ServicoGateway servicoGateway,
                                  ChecklistGateway checklistGateway,
                                  ItemChecklistGateway itemChecklistGateway,
                                  AuthenticatedUserProvider authenticatedUserProvider) {
        this.servicoGateway = servicoGateway;
        this.checklistGateway = checklistGateway;
        this.itemChecklistGateway = itemChecklistGateway;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @Transactional
    public ConcluirServicoResponse executar(Long servicoId) {
        var servico = servicoGateway.buscarPorId(servicoId)
                .orElseThrow(() -> new ServicoNaoEncontradoException(servicoId));

        validarMecanicoDono(servico);

        if (servico.getStatus() != StatusProcesso.EM_ANDAMENTO) {
            throw new ChecklistStatusInvalidoException(
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

        int itensMarcadosComoTrocaFeita = 0;
        int itensSemAutorizacao = 0;

        for (var item : checklist.getItensChecklist()) {
            if (item.getStatusItem() != StatusItem.TROCAR) {
                continue;
            }

            boolean possuiProdutoAutorizado = item.getProdutosOrcados().stream()
                    .anyMatch(produto -> Boolean.TRUE.equals(produto.getAprovadoCliente()));

            if (possuiProdutoAutorizado) {
                item.atualizarStatus(StatusItem.TROCA_FEITA);
                itemChecklistGateway.salvar(item);
                itensMarcadosComoTrocaFeita++;
            } else {
                itensSemAutorizacao++;
            }
        }

        servico.atualizarStatus(StatusProcesso.CONCLUIDO);
        servico.atualizarDataRealizacao(LocalDate.now());
        checklist.finalizar();

        servicoGateway.salvar(servico);
        checklistGateway.salvar(checklist);

        return new ConcluirServicoResponse(
                servico.getId(),
                checklist.getId(),
                itensMarcadosComoTrocaFeita,
                itensSemAutorizacao,
                servico.getStatus(),
                checklist.getStatus());
    }

    private void validarMecanicoDono(com.meclist.domain.Servico servico) {
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
