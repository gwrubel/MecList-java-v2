package com.meclist.usecase.mecanico;

import org.springframework.stereotype.Service;

import com.meclist.interfaces.MecanicoGateway;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AtualizarSituacaoMecanicoUseCase {
    private final MecanicoGateway mecanicoGateway;

    public AtualizarSituacaoMecanicoUseCase(MecanicoGateway mecanicoGateway) {
        this.mecanicoGateway = mecanicoGateway;
    }

    public void desativar(Long id) {
        var mecanico = mecanicoGateway.bucarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Mecânico não encontrado"));

        mecanico.desativar(); 

        mecanicoGateway.salvar(mecanico); 

    }

    public void ativar(Long id) {
        var mecanico = mecanicoGateway.bucarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Mecânico não encontrado"));

        mecanico.ativar(); 

        mecanicoGateway.salvar(mecanico); 

    }

    
}
