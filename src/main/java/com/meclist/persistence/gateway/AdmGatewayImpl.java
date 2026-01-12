package com.meclist.persistence.gateway;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.meclist.domain.Adm;
import com.meclist.interfaces.AdmGateway;
import com.meclist.mapper.AdmMapper;
import com.meclist.persistence.repository.AdmRepository;

@Component
public class AdmGatewayImpl implements AdmGateway {
    private final AdmRepository repository;

    public AdmGatewayImpl(AdmRepository repository) {
        this.repository = repository;
    }

    @Override
    public Adm cadastrarAdm(Adm adm) {
        return AdmMapper.toDomain(repository.save(AdmMapper.toEntity(adm)));
    }

    @Override
    public Optional<Adm> buscarPorEmail(String email) {
        return repository.findByEmail(email).map(AdmMapper::toDomain);
    }
}
