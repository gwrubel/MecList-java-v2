package com.meclist.usecase.mecanico;

import org.springframework.stereotype.Service;

import com.meclist.domain.Mecanico;
import com.meclist.interfaces.MecanicoGateway;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AtualizarSituacaoMecanicoUseCase {
    private final MecanicoGateway mecanicoGateway;

    public AtualizarSituacaoMecanicoUseCase(MecanicoGateway mecanicoGateway) {
        this.mecanicoGateway = mecanicoGateway;
    }

    public Mecanico desativar(Long id) {
        var mecanico = mecanicoGateway.bucarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Mecânico não encontrado"));

        mecanico.desativar(); 

       return mecanicoGateway.salvar(mecanico); 

    }

    public Mecanico ativar(Long id) {
        var mecanico = mecanicoGateway.bucarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Mecânico não encontrado"));

        mecanico.ativar(); 

       return mecanicoGateway.salvar(mecanico); 

    }

    
}
