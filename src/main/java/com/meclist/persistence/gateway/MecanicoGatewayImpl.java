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


@Component
public class MecanicoGatewayImpl implements MecanicoGateway {
    private final MecanicoRepository repository;

    public MecanicoGatewayImpl(MecanicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mecanico salvar(Mecanico mecanico) {
        return MecanicoMapper.toDomain(repository.save(MecanicoMapper.toEntity(mecanico)));
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
    public Mecanico atualizarMecanico(Mecanico mecanico) {
        
        return MecanicoMapper.toDomain(repository.save(MecanicoMapper.toEntity(mecanico)));
    }

    @Override
    public List<Mecanico> buscarPorSituacao(Situacao situacao) {
        return repository.findBySituacao(situacao)
                .stream()
                .map(MecanicoMapper::toDomain)
                .collect(Collectors.toList());
    }
}