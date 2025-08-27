package com.meclist.usecase.veiculo;

import org.springframework.stereotype.Service;

import com.meclist.dto.veiculo.VeiculoRequestDTO;
import com.meclist.interfaces.VeiculoGateway;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AtualizarVeiculoUseCase {

    private final VeiculoGateway veiculoGateway;

    public AtualizarVeiculoUseCase(VeiculoGateway veiculoGateway) {
        this.veiculoGateway = veiculoGateway;
    }

    public void atualizarVeiculo(Long idCliente, Long idVeiculo, VeiculoRequestDTO request) {
        var veiculoExistente = veiculoGateway.buscarVeiculoPorId(idVeiculo);

        if (veiculoExistente.isEmpty()) {
            throw new EntityNotFoundException("Veículo com ID " + idVeiculo + " não encontrado!");
        }

        var veiculo = veiculoExistente.get();

        // Validação: veiculo deve pertencer ao cliente informado
        if (!veiculo.getCliente().getId().equals(idCliente)) {
            throw new RuntimeException("Veículo não pertence ao cliente informado.");
        }

        
        if (request.placa() != null) veiculo.atualizarPlaca(request.placa());
        if (request.modelo() != null) veiculo.atualizarModelo(request.modelo());
        if (request.marca() != null) veiculo.atualizarMarca(request.marca());
        if (request.ano() != null) veiculo.atualizarAno(request.ano());
        if (request.cor() != null) veiculo.atualizarCor(request.cor());
        if (request.quilometragem() != 0) veiculo.atualizarQuilometragem(veiculo.getQuilometragem(), request.quilometragem());
        

        veiculoGateway.atualizarVeiculo(veiculo);
        
    }
}
