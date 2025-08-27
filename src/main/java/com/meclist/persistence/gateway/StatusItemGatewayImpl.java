package com.meclist.persistence.gateway;

import com.meclist.domain.StatusItem;
import com.meclist.interfaces.StatusItemGateway;
import com.meclist.mapper.StatusItemMapper; // Você precisará criar este mapper
import com.meclist.persistence.repository.StatusItemRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StatusItemGatewayImpl implements StatusItemGateway {

    private final StatusItemRepository statusItemRepository;

    public StatusItemGatewayImpl(StatusItemRepository statusItemRepository) {
        this.statusItemRepository = statusItemRepository;
    }

    @Override
    public Optional<StatusItem> buscarPorDescricao(String descricao) {
        return statusItemRepository.findByDescricao(descricao)
                .map(StatusItemMapper::toDomain); // Mapeia de Entity para Domain se presente
    }

    @Override
    public Optional<StatusItem> buscarPorId(Long id) {
        return statusItemRepository.findById(id).map(StatusItemMapper::toDomain);
    }
}