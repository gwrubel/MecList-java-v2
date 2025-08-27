package com.meclist.usecase.cliente;

import org.springframework.stereotype.Service;

import com.meclist.dto.cliente.ClienteResponse;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.interfaces.VeiculoGateway;
import com.meclist.mapper.ClienteMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BuscarDadosDoClienteUseCase {

    private final ClienteGateway clienteGateway;
    private final VeiculoGateway veiculoGateway;

    public BuscarDadosDoClienteUseCase(ClienteGateway clienteGateway, VeiculoGateway veiculoGateway) {
        this.clienteGateway = clienteGateway;
        this.veiculoGateway = veiculoGateway;
    }

    public ClienteResponse buscarDadosDoCliente(Long id) {
        var cliente = clienteGateway.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        // Para cada veículo, buscar a data da última revisão
        cliente.getVeiculos().forEach(veiculo -> {
            veiculoGateway.buscarVeiculoPorId(veiculo.getId())
                .ifPresent(v -> veiculo.atualizarDataUltimaRevisao(v.getDataUltimaRevisao()));
        });

        return ClienteMapper.toResponse(cliente);
    }
}
