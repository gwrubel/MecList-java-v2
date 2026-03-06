package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.meclist.domain.Cliente;
import com.meclist.domain.Veiculo;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.interfaces.VeiculoGateway;
import com.meclist.mapper.ClienteMapper;
import com.meclist.mapper.VeiculoMapper;
import com.meclist.persistence.entity.ClienteEntity;
import com.meclist.persistence.entity.VeiculoEntity;
import com.meclist.persistence.repository.ClienteRepository;
import com.meclist.persistence.repository.ServicoRepository;
import com.meclist.persistence.repository.VeiculoRepository;

@Component
public class VeiculoGatewayImpl implements VeiculoGateway {

    private final VeiculoRepository repository;
    private final ClienteRepository clienteRepository;
    private final ServicoRepository servicoRepository;

    public VeiculoGatewayImpl(VeiculoRepository repository,
            ClienteRepository clienteRepository,
            ServicoRepository servicoRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.servicoRepository = servicoRepository;
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
        VeiculoEntity entity = repository.findById(veiculo.getId())
                .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado: " + veiculo.getId()));

        ClienteEntity clienteEntity = clienteRepository.findById(veiculo.getCliente().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + veiculo.getCliente().getId()));

        
        entity.setPlaca(veiculo.getPlaca());
        entity.setMarca(veiculo.getMarca());
        entity.setModelo(veiculo.getModelo());
        entity.setAno(veiculo.getAno());
        entity.setCor(veiculo.getCor());
        entity.setQuilometragem(veiculo.getQuilometragem());
        entity.setCliente(clienteEntity);

        VeiculoEntity salvo = repository.save(entity);
        return VeiculoMapper.toDomain(salvo, ClienteMapper.toDomain(salvo.getCliente()));
    }

    @Override
    public Optional<Veiculo> buscarVeiculoPorId(Long id) {
        return repository.findById(id)
                .map(entity -> VeiculoMapper.toDomain(
                        entity,
                        ClienteMapper.toDomain(entity.getCliente())));
    }

    @Override
    public List<Veiculo> buscarVeiculosPorCliente(Long clienteId) {
        ClienteEntity clienteEntity = clienteRepository.findById(clienteId).get();
        Cliente cliente = ClienteMapper.toDomain(clienteEntity);
        return cliente.getVeiculos();
    }

    @Override
    public List<Veiculo> buscarPlacasPorTrecho(String trecho) {
        return repository.findTop10ByPlacaContainingIgnoreCase(trecho)
                .stream()
                .map(this::mapearVeiculoComUltimaRevisao)
                .toList();
    }

    @Override
    public Optional<Veiculo> buscarPorPlaca(String placa) {
        return repository.findByPlaca(placa)
                .map(this::mapearVeiculoComUltimaRevisao);
    }

    private Veiculo mapearVeiculoComUltimaRevisao(VeiculoEntity entity) {
        Veiculo veiculo = VeiculoMapper.toDomain(entity, ClienteMapper.toDomain(entity.getCliente()));

        servicoRepository.encontrarUltimaRevisao(entity.getId(), StatusProcesso.CONCLUIDO)
                .ifPresent(servico -> {
                    veiculo.atualizarDataUltimaRevisao(
                            servico.getDataRealizacao().toInstant()
                                    .atZone(java.time.ZoneId.systemDefault())
                                    .toLocalDate());
                });

        return veiculo;
    }

}