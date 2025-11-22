package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.meclist.domain.Cliente;
import com.meclist.domain.Veiculo;
import com.meclist.interfaces.VeiculoGateway;

import jakarta.persistence.EntityNotFoundException;
import com.meclist.mapper.ClienteMapper;
import com.meclist.mapper.VeiculoMapper;
import com.meclist.persistence.entity.ClienteEntity;
import com.meclist.persistence.entity.VeiculoEntity;
import com.meclist.persistence.repository.ClienteRepository;
import com.meclist.persistence.repository.VeiculoRepository;

@Component
public class VeiculoGatewayImpl implements VeiculoGateway {

    private static final Logger log = LoggerFactory.getLogger(VeiculoGatewayImpl.class);

    private final VeiculoRepository repository;
    private final ClienteRepository clienteRepository;

    public VeiculoGatewayImpl(VeiculoRepository repository, ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
        this.repository = repository;
    }

    @Override
    public void salvarVeiculo(Veiculo veiculo) {
        ClienteEntity clienteEntity = clienteRepository.findById(veiculo.getCliente().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        repository.save(VeiculoMapper.toEntity(veiculo, clienteEntity));
    }

    @Override
    public void atualizarVeiculo(Veiculo veiculo) {
        VeiculoEntity entityExistente = repository.findById(veiculo.getId())
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));

        ClienteEntity clienteEntity = entityExistente.getCliente();

        VeiculoEntity entityAtualizada = VeiculoMapper.toEntity(veiculo, clienteEntity);
        entityAtualizada.setId(entityExistente.getId());

        repository.save(entityAtualizada);
    }

    @Override
    public Optional<Veiculo> buscarVeiculoPorId(Long id) {
        return repository.findById(id)
                .map(entity -> {
                    ClienteEntity clienteEntity = entity.getCliente();
                    Cliente cliente = ClienteMapper.toDomain(clienteEntity);
                    Veiculo veiculo = VeiculoMapper.toDomain(entity, cliente);
                    return veiculo;
                });
    }

    public List<Veiculo> buscarVeiculosPorCliente(Long clienteId) {
        ClienteEntity clienteEntity = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        Cliente cliente = ClienteMapper.toDomain(clienteEntity);

        log.info("Veículos do cliente {}: {}", cliente.getNome(), cliente.getVeiculos());
        return cliente.getVeiculos();
    }

    @Override
    public List<String> buscarPlacasPorTrecho(String trecho) {
        return repository.findTop10ByPlacaContainingIgnoreCase(trecho)
                .stream()
                .map(VeiculoEntity::getPlaca)
                .toList();
    }

    @Override
    public Optional<Veiculo> buscarPorPlaca(String placa) {
        return repository.findByPlaca(placa)
                .map(entity -> {
                    ClienteEntity clienteEntity = entity.getCliente();
                    Cliente cliente = ClienteMapper.toDomain(clienteEntity);
                    Veiculo veiculo = VeiculoMapper.toDomain(entity, cliente);
                    return veiculo;
                });
    }
}