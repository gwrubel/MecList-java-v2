package com.meclist.persistence.gateway;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.ItemChecklist;
import com.meclist.interfaces.ItemChecklistGateway;
import com.meclist.persistence.entity.ItemChecklistEntity;
import com.meclist.persistence.repository.ItemChecklistRepository;

@Component
public class ItemChecklistGatewayImpl implements ItemChecklistGateway {

    private final ItemChecklistRepository itemChecklistRepository;

    public ItemChecklistGatewayImpl(ItemChecklistRepository itemChecklistRepository) {
        this.itemChecklistRepository = itemChecklistRepository;
    }

    @Override
    public List<ItemChecklist> buscarPorChecklist(Long idChecklist) {
        return itemChecklistRepository.findByChecklist(idChecklist)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    private ItemChecklist toDomain(ItemChecklistEntity entity) {
        return new ItemChecklist(
                entity.getId(),
                entity.getChecklist().getId(),
                entity.getItem().getId(),
                entity.getStatusItem().getId(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
}