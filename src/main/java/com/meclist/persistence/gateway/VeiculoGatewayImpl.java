package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.meclist.domain.Cliente;
import com.meclist.domain.Veiculo;
import com.meclist.interfaces.VeiculoGateway;
import com.meclist.mapper.ClienteMapper;
import com.meclist.mapper.VeiculoMapper;
import com.meclist.persistence.entity.ClienteEntity;
import com.meclist.persistence.entity.VeiculoEntity;
import com.meclist.persistence.repository.ClienteRepository;
import com.meclist.persistence.repository.VeiculoRepository;

@Component
public class VeiculoGatewayImpl implements VeiculoGateway {

    private final VeiculoRepository repository;
    private final ClienteRepository clienteRepository;

    public VeiculoGatewayImpl(VeiculoRepository repository,
                              ClienteRepository clienteRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Veiculo salvarVeiculo(Veiculo veiculo) {
        ClienteEntity clienteEntity = clienteRepository.findById(veiculo.getCliente().getId()).get();
        VeiculoEntity entity = VeiculoMapper.toEntity(veiculo, clienteEntity);
        repository.save(entity);
        return VeiculoMapper.toDomain(entity, veiculo.getCliente());
    }

    @Override
    public Veiculo atualizarVeiculo(Veiculo veiculo) {
        ClienteEntity clienteEntity = clienteRepository.findById(veiculo.getCliente().getId()).get();
        VeiculoEntity entity = VeiculoMapper.toEntity(veiculo, clienteEntity);
        entity.setId(veiculo.getId());
        repository.save(entity);
        return VeiculoMapper.toDomain(entity, veiculo.getCliente());
    }

    @Override
    public Optional<Veiculo> buscarVeiculoPorId(Long id) {
        return repository.findById(id)
                .map(entity -> VeiculoMapper.toDomain(
                        entity,
                        ClienteMapper.toDomain(entity.getCliente())
                ));
    }

    @Override
    public List<Veiculo> buscarVeiculosPorCliente(Long clienteId) {
        ClienteEntity clienteEntity = clienteRepository.findById(clienteId).get();
        Cliente cliente = ClienteMapper.toDomain(clienteEntity);
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
                .map(entity -> VeiculoMapper.toDomain(
                        entity,
                        ClienteMapper.toDomain(entity.getCliente())
                ));
    }
}
