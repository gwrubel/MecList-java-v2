package com.meclist.usecase.veiculo;

import org.springframework.stereotype.Service;

import com.meclist.domain.Veiculo;
import com.meclist.dto.veiculo.VeiculoRequestDTO;
import com.meclist.exception.VeiculoJaCadastrado;
import com.meclist.interfaces.VeiculoGateway;
import com.meclist.mapper.ClienteMapper;
import com.meclist.mapper.VeiculoMapper;
import com.meclist.persistence.entity.ClienteEntity;
import com.meclist.persistence.repository.ClienteRepository;
import com.meclist.validator.ValidatorUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CadastrarVeiculoUseCase {

    private final VeiculoGateway veiculoGateway;
    private final ClienteRepository clienteRepository;

    public CadastrarVeiculoUseCase(VeiculoGateway veiculoGateway, ClienteRepository clienteRepository) {
        this.veiculoGateway = veiculoGateway;
        this.clienteRepository = clienteRepository;
    }

    public void cadastarVeiculo(Long clienteId, VeiculoRequestDTO request) {
        ClienteEntity clienteEntity = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        ValidatorUtils.validarPlaca(request.placa());

        if (veiculoGateway.buscarPorPlaca(request.placa()).isPresent()) {
            throw new VeiculoJaCadastrado("Já existe um veículo cadastrado com a placa: " + request.placa());
        }

        Veiculo veiculo = VeiculoMapper.toDomain(request, ClienteMapper.toDomain(clienteEntity));

        veiculoGateway.salvarVeiculo(veiculo);
    }
}