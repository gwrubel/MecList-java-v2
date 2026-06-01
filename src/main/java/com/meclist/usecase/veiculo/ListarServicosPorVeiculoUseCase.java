package com.meclist.usecase.veiculo;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.meclist.domain.Checklist;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.veiculo.HistoricoServicoCard;
import com.meclist.exception.AcessoNegadoException;
import com.meclist.exception.VeiculoNaoEncontrado;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ServicoGateway;
import com.meclist.interfaces.VeiculoGateway;
import com.meclist.security.AuthenticatedUserProvider;

@Service
public class ListarServicosPorVeiculoUseCase {

    private final ChecklistGateway checklistGateway;
    private final ServicoGateway servicoGateway;
    private final VeiculoGateway veiculoGateway;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public ListarServicosPorVeiculoUseCase(ChecklistGateway checklistGateway,
                                           ServicoGateway servicoGateway,
                                           VeiculoGateway veiculoGateway,
                                           AuthenticatedUserProvider authenticatedUserProvider) {
        this.checklistGateway = checklistGateway;
        this.servicoGateway = servicoGateway;
        this.veiculoGateway = veiculoGateway;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    public List<HistoricoServicoCard> executar(Long veiculoId) {
        var user = authenticatedUserProvider.get();

        var veiculo = veiculoGateway.buscarVeiculoPorId(veiculoId)
                .orElseThrow(() -> new VeiculoNaoEncontrado("Veiculo nao encontrado: " + veiculoId));

        switch (user.role()) {
            case "CLIENTE" -> {
                Long clienteIdVeiculo = veiculo.getCliente() != null ? veiculo.getCliente().getId() : null;
                if (clienteIdVeiculo == null || !clienteIdVeiculo.equals(user.id())) {
                    throw new AcessoNegadoException("Este veiculo pertence a outro cliente.");
                }
            }
            case "MECANICO" -> { /* sem restrição de ownership */ }
            default -> throw new AcessoNegadoException("Acesso negado para o perfil: " + user.role());
        }

        return checklistGateway.buscarPorVeiculoId(veiculoId).stream()
                .filter(c -> c.getStatus() == StatusProcesso.CONCLUIDO)
                .sorted(Comparator.comparing(Checklist::getAtualizadoEm,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toHistoricoCard)
                .toList();
    }

    private HistoricoServicoCard toHistoricoCard(Checklist checklist) {
        var servico = servicoGateway.buscarPorChecklistId(checklist.getId()).stream()
                .filter(s -> s.getStatus() == StatusProcesso.CONCLUIDO)
                .filter(s -> s.getDataConclusao() != null)
                .max(Comparator.comparing(s -> s.getDataConclusao()))
                .orElse(null);

        String mecanico = servico != null && servico.getMecanico() != null
                ? servico.getMecanico().getNome()
                : null;

        return new HistoricoServicoCard(
                checklist.getId(),
                checklist.getDescricao(),
                checklist.getQuilometragem(),
                servico != null ? servico.getDataConclusao() : null,
                mecanico);
    }
}
