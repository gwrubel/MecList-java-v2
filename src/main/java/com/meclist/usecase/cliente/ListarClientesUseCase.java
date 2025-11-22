package com.meclist.usecase.cliente;




import com.meclist.dto.cliente.ClienteResponse;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.mapper.ClienteMapper;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarClientesUseCase {

    private final ClienteGateway clienteGateway;

    public ListarClientesUseCase(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }

    public List<ClienteResponse> listar() {
        return clienteGateway.buscarTodos()
        .stream()
        .map(ClienteMapper::toResponse)
        .toList();
                
    }
}
