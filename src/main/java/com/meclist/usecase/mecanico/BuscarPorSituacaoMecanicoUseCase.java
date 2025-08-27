package com.meclist.usecase.mecanico;

import java.util.List;

import org.springframework.stereotype.Service;


import com.meclist.domain.enums.Situacao;
import com.meclist.dto.mecanico.MecanicoResponse;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.mapper.MecanicoMapper;

@Service
public class BuscarPorSituacaoMecanicoUseCase {
    private final MecanicoGateway mecanicoGateway;

    public BuscarPorSituacaoMecanicoUseCase(MecanicoGateway mecanicoGateway) {
        this.mecanicoGateway = mecanicoGateway;
    }

    public List<MecanicoResponse> buscarPorSituacao (Situacao situacao) {
        return mecanicoGateway.buscarPorSituacao(situacao)
         .stream()
        .map(MecanicoMapper::toResponse)
        .toList();
    }
}
