package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.Checklist;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.mapper.ChecklistMapper;
import com.meclist.persistence.entity.ChecklistEntity;
import com.meclist.persistence.repository.ChecklistRepository;
import com.meclist.persistence.repository.ItemChecklistRepository;

import jakarta.transaction.Transactional;

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
    @Transactional // Garante que tudo ocorra na mesma transação
    public Checklist salvar(Checklist checklist) {
        // 1. Se o checklist já existe, buscamos a entidade "gerenciada" pelo Hibernate
        ChecklistEntity entityParaSalvar;
        if (checklist.getId() != null) {
            entityParaSalvar = checklistRepository.findById(checklist.getId())
                    .orElse(new ChecklistEntity());

            // 2. Atualizamos os campos da entidade gerenciada com os dados do domínio
            atualizarDadosEntidade(entityParaSalvar, checklist);
        } else {
            entityParaSalvar = ChecklistMapper.toEntity(checklist);
        }

        // 3. O save agora funciona em uma entidade que o Hibernate já conhece
        var salvo = checklistRepository.save(entityParaSalvar);

        // 4. Forçamos o flush para garantir que os IDs de orçamentos/itens sejam
        // gerados
        checklistRepository.flush();

        var itens = itemChecklistRepository.findByChecklistId(salvo.getId());
        return ChecklistMapper.toDomain(salvo, itens);
    }

    private void atualizarDadosEntidade(ChecklistEntity entity, Checklist domain) {
        entity.setStatus(domain.getStatus());
        entity.setQuilometragem(domain.getQuilometragem());
        entity.setDescricao(domain.getDescricao());
        entity.setAtualizadoEm(domain.getAtualizadoEm());

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

    @Override
    @Transactional
    public void atualizarStatus(Checklist checklist) {

        var entity = checklistRepository.findById(checklist.getId())
                .orElseThrow(() -> new RuntimeException("Checklist não encontrado: " + checklist.getId()));
        // Altera apenas o status e data
        entity.setStatus(checklist.getStatus());
        entity.setAtualizadoEm(checklist.getAtualizadoEm());

        // Salva (sem risco de transient)
        checklistRepository.save(entity);

    }

    @Override
    public List<Checklist> buscarPorStatus(StatusProcesso status) {
        return checklistRepository.findByStatus(status)
                .stream()
                .map(ChecklistMapper::toDomain)
                .collect(Collectors.toList());
    }
}