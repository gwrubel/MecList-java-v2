package com.meclist.usecase.veiculo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meclist.domain.Veiculo;
import com.meclist.dto.veiculo.VeiculoResponse;
import com.meclist.interfaces.VeiculoGateway;
import com.meclist.mapper.VeiculoMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BuscarPorPlacaUseCase {
     private final VeiculoGateway veiculoGateway;

    public BuscarPorPlacaUseCase(VeiculoGateway veiculoGateway) {
        this.veiculoGateway = veiculoGateway;
    }

    
    public List<String> executar(String trecho) {
        return veiculoGateway.buscarPlacasPorTrecho(trecho);
    }

    public VeiculoResponse buscarPorPlaca(String placa) {
        return veiculoGateway.buscarPorPlaca(placa)
                .map(VeiculoMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado com a placa: " + placa));
    }
}
