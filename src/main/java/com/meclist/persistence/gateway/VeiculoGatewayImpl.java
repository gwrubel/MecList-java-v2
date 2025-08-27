package com.meclist.persistence.gateway;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.meclist.domain.Cliente;
import com.meclist.domain.Veiculo;
import com.meclist.exception.CampoInvalidoException;
import com.meclist.exception.VeiculoJaCadastrado;
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
    

    public VeiculoGatewayImpl(VeiculoRepository repository, ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
        this.repository = repository;
    }

    @Override
    public void salvarVeiculo(Veiculo veiculo) {
        ClienteEntity clienteEntity = clienteRepository.findById(veiculo.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (repository.findByPlaca(veiculo.getPlaca()).isPresent()) {
            throw new CampoInvalidoException(Map.of("placa", "Placa já cadastrado!"));
        }

        repository.save(VeiculoMapper.toEntity(veiculo, clienteEntity));
    }

    @Override
    public void atualizarVeiculo(Veiculo veiculo) {

        VeiculoEntity entityExistente = repository.findById(veiculo.getId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        // Verifica se o veículo realmente pertence ao cliente informado
        if (!entityExistente.getCliente().getId().equals(veiculo.getCliente().getId())) {
            throw new RuntimeException("Veículo não pertence ao cliente informado");
        }

        // Busca o cliente para atualização (opcional, se for usar entidade atualizada)
        ClienteEntity clienteEntity = clienteRepository.findById(veiculo.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Atualiza os campos do veículo
        VeiculoEntity entityAtualizada = VeiculoMapper.toEntity(veiculo, clienteEntity);
        entityAtualizada.setId(entityExistente.getId()); // mantém o ID original

        // Salva a atualização
        repository.save(entityAtualizada);
    }

    @Override
    public Optional<Veiculo> buscarVeiculoPorId(Long id) {
        return repository.findById(id)
                .map(entity -> {
                    ClienteEntity clienteEntity = entity.getCliente();
                    Cliente cliente = ClienteMapper.toDomain(clienteEntity);

                    Veiculo veiculo = VeiculoMapper.toDomain(entity, cliente);

                    // Sem integração com serviços, data da última revisão permanece conforme persistido

                    return veiculo;
                });
    }

    public List<Veiculo> buscarVeiculosPorCliente(Long clienteId) {
        ClienteEntity clienteEntity = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Cliente cliente = ClienteMapper.toDomain(clienteEntity); // sem dataUltimaRevisao

        // Retorna veículos do cliente sem consulta de revisão
        System.out.println("Veículos do cliente " + cliente.getNome() + ": " + cliente.getVeiculos());
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

                    // Sem integração com serviços, data da última revisão permanece conforme persistido

                    return veiculo;
                });
    }
}
