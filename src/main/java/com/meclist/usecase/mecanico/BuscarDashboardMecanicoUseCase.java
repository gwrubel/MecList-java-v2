package com.meclist.usecase.mecanico;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meclist.domain.Servico;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.mecanico.DashboardMecanicoResponse;
import com.meclist.dto.mecanico.ServicoCardMecanicoResponse;
import com.meclist.exception.AcessoNegadoException;
import com.meclist.exception.MecanicoNaoEncontradoException;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.interfaces.ServicoGateway;
import com.meclist.security.AuthenticatedUserProvider;

@Service
public class BuscarDashboardMecanicoUseCase {

    private final ServicoGateway servicoGateway;
    private final MecanicoGateway mecanicoGateway;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public BuscarDashboardMecanicoUseCase(ServicoGateway servicoGateway,
                                          MecanicoGateway mecanicoGateway,
                                          AuthenticatedUserProvider authenticatedUserProvider) {
        this.servicoGateway = servicoGateway;
        this.mecanicoGateway = mecanicoGateway;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    public DashboardMecanicoResponse executarDoUsuarioAutenticado() {
        var user = authenticatedUserProvider.get();
        if (!"MECANICO".equals(user.role()) && !"ADMIN".equals(user.role()) && !"ADM".equals(user.role())) {
            throw new AcessoNegadoException("Apenas mecânico ou administrador podem acessar o dashboard.");
        }
        return executarPorMecanicoId(user.id());
    }

    public DashboardMecanicoResponse executarPorMecanicoId(Long mecanicoId) {
        var mecanico = mecanicoGateway.bucarPorId(mecanicoId)
                .orElseThrow(() -> new MecanicoNaoEncontradoException(mecanicoId));

        List<ServicoCardMecanicoResponse> pendentes = servicoGateway
                .buscarPorMecanicoEStatuses(mecanicoId, List.of(StatusProcesso.EM_ANDAMENTO, StatusProcesso.ATRIBUIDO))
                .stream()
                .map(this::toCard)
                .toList();

        List<ServicoCardMecanicoResponse> concluidos = servicoGateway
                .buscarPorMecanicoEStatuses(mecanicoId, List.of(StatusProcesso.CONCLUIDO))
                .stream()
                .map(this::toCard)
                .toList();

        return new DashboardMecanicoResponse(
                mecanico.getId(),
                mecanico.getNome(),
                pendentes,
                concluidos);
    }

    private ServicoCardMecanicoResponse toCard(Servico servico) {
        var checklist = servico.getChecklist();
        var veiculo = checklist != null ? checklist.getVeiculo() : null;

        return new ServicoCardMecanicoResponse(
                servico.getId(),
                checklist != null ? checklist.getId() : null,
                servico.getStatus(),
                veiculo != null ? veiculo.getId() : null,
                veiculo != null ? veiculo.getPlaca() : null,
                veiculo != null ? veiculo.getMarca() : null,
                veiculo != null ? veiculo.getModelo() : null,
                veiculo != null ? veiculo.getAno() : null,
                checklist != null ? checklist.getQuilometragem() : null,
                servico.getCriadoEm(),
            servico.getDataConclusao());
    }
}
