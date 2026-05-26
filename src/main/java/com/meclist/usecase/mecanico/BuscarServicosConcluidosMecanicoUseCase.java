package com.meclist.usecase.mecanico;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meclist.dto.mecanico.ServicosConcluidoMecanicoResponse;
import com.meclist.exception.MecanicoNaoEncontradoException;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.interfaces.ServicoGateway;

@Service
public class BuscarServicosConcluidosMecanicoUseCase {

    private final ServicoGateway servicoGateway;
    private final MecanicoGateway mecanicoGateway;

    public BuscarServicosConcluidosMecanicoUseCase(ServicoGateway servicoGateway,
                                                    MecanicoGateway mecanicoGateway) {
        this.servicoGateway = servicoGateway;
        this.mecanicoGateway = mecanicoGateway;
    }

    public List<ServicosConcluidoMecanicoResponse> executar(Long mecanicoId) {
        mecanicoGateway.bucarPorId(mecanicoId)
                .orElseThrow(() -> new MecanicoNaoEncontradoException(mecanicoId));

        return servicoGateway.buscarConcluidosPorMecanico(mecanicoId)
                .stream()
                .map(servico -> {
                    var checklist = servico.getChecklist();
                    var veiculo = checklist != null ? checklist.getVeiculo() : null;
                    var cliente = veiculo != null ? veiculo.getCliente() : null;
                    return new ServicosConcluidoMecanicoResponse(
                            checklist != null ? checklist.getId() : null,
                            cliente != null ? cliente.getNome() : null,
                            veiculo != null ? veiculo.getPlaca() : null,
                            veiculo != null ? veiculo.getModelo() : null,
                            servico.getCriadoEm(),
                            servico.getDataConclusao());
                })
                .toList();
    }
}