package com.meclist.usecase.cliente;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meclist.dto.cliente.ChecklistCardResumo;
import com.meclist.dto.cliente.DashboardClienteResponse;
import com.meclist.dto.cliente.VeiculoResumo;
import com.meclist.exception.VeiculoNaoEncontrado;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.interfaces.VeiculoGateway;

@Service
public class BuscarDadosDashboardUseCase {
    private final ClienteGateway clienteGateway;
    private final VeiculoGateway veiculoGateway;
    private final ChecklistGateway checklistGateway;

    public BuscarDadosDashboardUseCase(ClienteGateway clienteGateway, VeiculoGateway veiculoGateway, ChecklistGateway checklistGateway) {
        this.clienteGateway = clienteGateway;
        this.veiculoGateway = veiculoGateway;
        this.checklistGateway = checklistGateway;
    }

    public DashboardClienteResponse buscarDados(Long clienteId) {
        var cliente = clienteGateway.buscarPorId(clienteId)
                .orElseThrow(() -> new VeiculoNaoEncontrado("Cliente não encontrado"));

        List<VeiculoResumo> veiculos = veiculoGateway.buscarVeiculosPorCliente(clienteId)
                .stream()
                .map(v -> new VeiculoResumo(
                        v.getId(),
                        v.getMarca(),
                        v.getModelo(),
                        v.getPlaca(),
                        v.getQuilometragem(),
                        v.getDataUltimaRevisao()
                ))
                .toList();

        List<ChecklistCardResumo> checklists = checklistGateway.buscarDashboardPorCliente(clienteId)
                .stream()
                .map(c -> new ChecklistCardResumo(
                        c.getId(),
                        c.getVeiculo().getPlaca(),
                        c.getVeiculo().getMarca(),
                        c.getVeiculo().getModelo(),
                        c.getVeiculo().getAno(),
                        c.getDescricao(),
                        c.getMecanico().getNome(),
                        c.getVeiculo().getQuilometragem(),
                        c.getStatus()
                ))
                .toList();

        return new DashboardClienteResponse(cliente.getNome(), veiculos, checklists);
    }

}
