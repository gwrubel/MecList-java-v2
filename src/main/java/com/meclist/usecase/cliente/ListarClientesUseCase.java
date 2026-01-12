package com.meclist.usecase.cliente;




import com.meclist.dto.cliente.ClienteListResponse;
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

    public List<ClienteListResponse> listar() {
        return clienteGateway.buscarTodos()
        .stream()
        .map(ClienteMapper::toListResponse)
        .toList();
                
    }
}
