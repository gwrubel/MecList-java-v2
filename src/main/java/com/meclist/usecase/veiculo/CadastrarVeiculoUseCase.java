package com.meclist.usecase.veiculo;





import org.springframework.stereotype.Service;


import com.meclist.domain.Veiculo;
import com.meclist.dto.veiculo.VeiculoRequestDTO;
import com.meclist.interfaces.VeiculoGateway;
import com.meclist.mapper.ClienteMapper;
import com.meclist.mapper.VeiculoMapper;
import com.meclist.persistence.entity.ClienteEntity;
import com.meclist.persistence.repository.ClienteRepository;

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
        // Busca o cliente
        ClienteEntity clienteEntity = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));


        Veiculo veiculo = VeiculoMapper.toDomain(request, ClienteMapper.toDomain(clienteEntity));

        // Salva
        veiculoGateway.salvarVeiculo(veiculo);
    }
}
