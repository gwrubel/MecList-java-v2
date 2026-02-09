package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.ItemChecklist;
import com.meclist.interfaces.ItemChecklistGateway;
import com.meclist.mapper.ItemChecklistMapper;
import com.meclist.mapper.ItemMapper;
import com.meclist.persistence.entity.ChecklistEntity;
import com.meclist.persistence.entity.ItemChecklistEntity;
import com.meclist.persistence.repository.ChecklistRepository;
import com.meclist.persistence.repository.ItemChecklistRepository;
import com.meclist.persistence.repository.ItemRepository;

@Component
public class ItemChecklistGatewayImpl implements ItemChecklistGateway {

    private final ItemChecklistRepository itemChecklistRepository;
    private final ChecklistRepository checklistRepository;
    private final ItemRepository itemRepository;

    public ItemChecklistGatewayImpl(ItemChecklistRepository itemChecklistRepository,
                                    ChecklistRepository checklistRepository,
                                    ItemRepository itemRepository) {
        this.itemChecklistRepository = itemChecklistRepository;
        this.checklistRepository = checklistRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemChecklist salvar(ItemChecklist itemChecklist) {
        ItemChecklistEntity entity;
        
        if (itemChecklist.getId() != null) {
            entity = itemChecklistRepository.findById(itemChecklist.getId())
                    .orElseThrow(() -> new IllegalArgumentException("ItemChecklist não encontrado: " + itemChecklist.getId()));
        } else {
            entity = new ItemChecklistEntity();
            entity.setCriadoEm(itemChecklist.getCriadoEm());
        }

        // Carrega as entidades relacionadas
        ChecklistEntity checklistEntity = checklistRepository.findById(itemChecklist.getChecklist().getId())
                .orElseThrow(() -> new IllegalArgumentException("Checklist não encontrado"));
        
        entity.setChecklist(checklistEntity);
        entity.setItem(ItemMapper.toEntity(itemChecklist.getItem()));
        entity.setStatusItem(itemChecklist.getStatusItem());
        entity.setAtualizadoEm(itemChecklist.getAtualizadoEm());

        ItemChecklistEntity salvo = itemChecklistRepository.save(entity);
        return ItemChecklistMapper.toDomain(salvo);
    }

    @Override
    public Optional<ItemChecklist> buscarPorId(Long id) {
        return itemChecklistRepository.findById(id)
                .map(ItemChecklistMapper::toDomain);
    }

    @Override
    public List<ItemChecklist> buscarPorChecklist(Long checklistId) {
        return itemChecklistRepository.findByChecklistId(checklistId)
                .stream()
                .map(ItemChecklistMapper::toDomain)
                .collect(Collectors.toList());
    }
}