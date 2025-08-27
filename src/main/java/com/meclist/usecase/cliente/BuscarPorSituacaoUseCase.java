package com.meclist.usecase.cliente;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meclist.domain.enums.Situacao;
import com.meclist.dto.cliente.ClienteResponse;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.mapper.ClienteMapper;

@Service
public class BuscarPorSituacaoUseCase {
    private final ClienteGateway clienteGateway;


    public BuscarPorSituacaoUseCase(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }


    public List <ClienteResponse> buscarPorSituacao(Situacao situacao) {
        return clienteGateway.buscarPorSituacao(situacao)
                .stream()
                .map(ClienteMapper::toResponse)
                .toList();
    }
}
