package com.meclist.persistence.gateway;

import com.meclist.domain.Status;
import com.meclist.interfaces.StatusGateway;
import com.meclist.mapper.StatusMapper;
import com.meclist.persistence.repository.StatusRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StatusGatewayImpl implements StatusGateway {

    private final StatusRepository statusRepository;

    public StatusGatewayImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public Optional<Status> buscarPorId(Long id) {
        return statusRepository.findById(id).map(StatusMapper::toDomain);
    }

    @Override
    public Optional<Status> buscarPorDescricao(String descricao) {
        return statusRepository.findByDescricao(descricao)
                .map(StatusMapper::toDomain);
    }
}
