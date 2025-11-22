package com.meclist.usecase.veiculo;

import org.springframework.stereotype.Service;

import com.meclist.dto.veiculo.AtualizarVeiculoRequestDTO;
import com.meclist.exception.VeiculoJaCadastrado;
import com.meclist.interfaces.VeiculoGateway;
import com.meclist.validator.ValidatorUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AtualizarVeiculoUseCase {

    private final VeiculoGateway veiculoGateway;

    public AtualizarVeiculoUseCase(VeiculoGateway veiculoGateway) {
        this.veiculoGateway = veiculoGateway;
    }

    public void atualizarVeiculo(Long idCliente, Long idVeiculo, AtualizarVeiculoRequestDTO request) {
        var veiculo = veiculoGateway.buscarVeiculoPorId(idVeiculo)
                .orElseThrow(() -> new EntityNotFoundException("Veículo com ID " + idVeiculo + " não encontrado!"));

        if (!veiculo.getCliente().getId().equals(idCliente)) {
            throw new RuntimeException("Veículo não pertence ao cliente informado.");
        }

        if (request.placa() != null) {
            ValidatorUtils.validarPlaca(request.placa());
            
            if (!veiculo.getPlaca().equals(request.placa()) && 
                veiculoGateway.buscarPorPlaca(request.placa()).isPresent()) {
                throw new VeiculoJaCadastrado("Já existe um veículo cadastrado com a placa: " + request.placa());
            }
            
            veiculo.atualizarPlaca(request.placa());
        }

        if (request.modelo() != null)
            veiculo.atualizarModelo(request.modelo());
        if (request.marca() != null)
            veiculo.atualizarMarca(request.marca());
        if (request.ano() != null)
            veiculo.atualizarAno(request.ano());
        if (request.cor() != null)
            veiculo.atualizarCor(request.cor());
        if (request.quilometragem() != 0)
            veiculo.atualizarQuilometragem(veiculo.getQuilometragem(), request.quilometragem());

        veiculoGateway.atualizarVeiculo(veiculo);
    }
}