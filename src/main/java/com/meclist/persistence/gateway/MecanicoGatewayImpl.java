package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.Mecanico;
import com.meclist.domain.enums.Situacao;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.mapper.MecanicoMapper;
import com.meclist.persistence.repository.MecanicoRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class MecanicoGatewayImpl implements MecanicoGateway {
    private final MecanicoRepository repository;

    public MecanicoGatewayImpl(MecanicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void salvar(Mecanico mecanico) {
        repository.save(MecanicoMapper.toEntity(mecanico));
    }

    @Override
    public Optional<Mecanico> buscarPorEmail(String email) {
        return repository.findByEmail(email).map(MecanicoMapper::toDomain);
    }

    @Override
    public Optional<Mecanico> buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf).map(MecanicoMapper::toDomain);
    }

    @Override
    public List<Mecanico> buscarTodos() {
        return repository.findAll()
                .stream()
                .map(MecanicoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Mecanico> bucarPorId(Long id) {
        return repository.findById(id).map(MecanicoMapper::toDomain);
    }

    @Override
    public void atualizarMecanico(Mecanico mecanico) {
        if (!repository.existsById(mecanico.getId())) {
            throw new EntityNotFoundException("Mecânico não encontrado");
        }

        repository.save(MecanicoMapper.toEntity(mecanico));
    }

    @Override
    public List<Mecanico> buscarPorSituacao(Situacao situacao) {
        return repository.findBySituacao(situacao)
                .stream()
                .map(MecanicoMapper::toDomain)
                .collect(Collectors.toList());
    }
}