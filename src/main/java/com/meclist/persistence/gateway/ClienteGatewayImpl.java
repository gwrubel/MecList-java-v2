package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.meclist.domain.Cliente;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.mapper.ClienteMapper;
import com.meclist.persistence.repository.ClienteRepository;


@Component
public class ClienteGatewayImpl implements ClienteGateway{
    private final ClienteRepository repository;

    public ClienteGatewayImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
       return ClienteMapper.toDomain(repository.save(ClienteMapper.toEntity(cliente)));
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return repository.findByEmail(email).map(ClienteMapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorDocumento(String documento) {
        return repository.findByDocumento(documento).map(ClienteMapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return repository.findById(id).map(ClienteMapper::toDomain);
    }

    @Override
    public List<Cliente> buscarTodos() {
        return repository.findAll()
                .stream()
                .map(ClienteMapper::toDomain)
                .toList();
    }

    @Override
    public Cliente atualizarCliente(Cliente cliente) {
       return ClienteMapper.toDomain(repository.save(ClienteMapper.toEntity(cliente)));
    }

    @Override
    public List<Cliente> buscarPorSituacao(com.meclist.domain.enums.Situacao situacao) {
        return repository.findBySituacao(situacao)
                .stream()
                .map(ClienteMapper::toDomain)
                .toList();
    }
}