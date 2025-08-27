package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.meclist.domain.Cliente;
import com.meclist.exception.CampoInvalidoException;


import com.meclist.interfaces.ClienteGateway;
import com.meclist.mapper.ClienteMapper;
import com.meclist.persistence.entity.ClienteEntity;
import com.meclist.persistence.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class ClienteJpaGatewayImpl implements ClienteGateway{
    private final ClienteRepository repository;

    public ClienteJpaGatewayImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void salvar(Cliente cliente) {
        repository.save(ClienteMapper.toEntity(cliente));
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
public void atualizarCliente(Cliente cliente) {
    if (!repository.existsById(cliente.getId())) {
        throw new EntityNotFoundException("Cliente não encontrado");
    }

    // Verificar se já existe outro cliente com o mesmo documento
    Optional<ClienteEntity> clienteComMesmoDocumento = repository.findByDocumento(cliente.getDocumento());
    if (clienteComMesmoDocumento.isPresent() && !clienteComMesmoDocumento.get().getId().equals(cliente.getId())) {
        throw new CampoInvalidoException(Map.of("documento", "Documento já cadastrado!"));
    }

    // Verificar se já existe outro cliente com o mesmo e-mail
    Optional<ClienteEntity> clienteComMesmoEmail = repository.findByEmail(cliente.getEmail());
    if (clienteComMesmoEmail.isPresent() && !clienteComMesmoEmail.get().getId().equals(cliente.getId())) {
       throw new CampoInvalidoException(Map.of("email", "E-mail já cadastrado!"));
    }

    // Persistir
    repository.save(ClienteMapper.toEntity(cliente));
}



@Override
public List<Cliente> buscarPorSituacao(com.meclist.domain.enums.Situacao situacao) {
    return repository.findBySituacao(situacao)
            .stream()
            .map(ClienteMapper::toDomain)
            .toList();
}
}
