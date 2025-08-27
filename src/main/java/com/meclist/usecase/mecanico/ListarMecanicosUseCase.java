package com.meclist.usecase.mecanico;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meclist.dto.mecanico.MecanicoResponse;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.mapper.MecanicoMapper;

@Service
public class ListarMecanicosUseCase {
    private final MecanicoGateway mecanicoGateway;

    public ListarMecanicosUseCase(MecanicoGateway mecanicoGateway) {
        this.mecanicoGateway = mecanicoGateway;
    }
    public List<MecanicoResponse> listarTodos() {
        return mecanicoGateway.buscarTodos()
        .stream()
        .map(MecanicoMapper::toResponse)
        .toList();
    }

    
}
