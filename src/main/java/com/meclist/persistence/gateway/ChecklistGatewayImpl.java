package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.Checklist;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.mapper.ChecklistMapper;
import com.meclist.persistence.repository.ChecklistRepository;
import com.meclist.persistence.repository.ItemChecklistRepository;

@Component
public class ChecklistGatewayImpl implements ChecklistGateway {

    private final ChecklistRepository checklistRepository;
    private final ItemChecklistRepository itemChecklistRepository;

    public ChecklistGatewayImpl(ChecklistRepository checklistRepository, 
                                ItemChecklistRepository itemChecklistRepository) {
        this.checklistRepository = checklistRepository;
        this.itemChecklistRepository = itemChecklistRepository;
    }

    @Override
    public Checklist salvar(Checklist checklist) {
        // Converte domain para entity usando mapper
        var entity = ChecklistMapper.toEntity(checklist);
        
        // Persiste no banco
        var salvo = checklistRepository.save(entity);
        
        // Carrega os itens do checklist
        var itens = itemChecklistRepository.findByChecklistId(salvo.getId());
        
        // Converte entity de volta para domain
        return ChecklistMapper.toDomain(salvo, itens);
    }

    @Override
    public Optional<Checklist> buscarPorId(Long id) {
        var entity = checklistRepository.findById(id);
        
        if (entity.isEmpty()) {
            return Optional.empty();
        }

        // Carrega os itens do checklist
        var itens = itemChecklistRepository.findByChecklistId(id);
        
        // Converte entity para domain
        return Optional.of(ChecklistMapper.toDomain(entity.get(), itens));
    }

    @Override
    public List<Checklist> buscarPorMecanico(Long mecanicoId) {
        return checklistRepository.findAll()
                .stream()
                .map(entity -> {
                    var itens = itemChecklistRepository.findByChecklistId(entity.getId());
                    return ChecklistMapper.toDomain(entity, itens);
                })
                .collect(Collectors.toList());
    }
}