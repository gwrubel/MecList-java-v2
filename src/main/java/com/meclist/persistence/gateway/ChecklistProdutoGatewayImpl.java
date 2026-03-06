package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.meclist.domain.ChecklistProduto;
import com.meclist.interfaces.ChecklistProdutoGateway;
import com.meclist.mapper.ChecklistProdutoMapper;
import com.meclist.persistence.entity.ChecklistProdutoEntity;
import com.meclist.persistence.repository.ChecklistProdutoRepository;

@Component
public class ChecklistProdutoGatewayImpl implements ChecklistProdutoGateway {

    private final ChecklistProdutoRepository checklistProdutoRepository;

    public ChecklistProdutoGatewayImpl(ChecklistProdutoRepository checklistProdutoRepository) {
        this.checklistProdutoRepository = checklistProdutoRepository;
    }

    @Override
    public ChecklistProduto salvar(ChecklistProduto checklistProduto) {
        ChecklistProdutoEntity entity = ChecklistProdutoMapper.toEntity(checklistProduto);
        ChecklistProdutoEntity salvo = checklistProdutoRepository.save(entity);
        return ChecklistProdutoMapper.toDomain(salvo);
    }

    @Override
    public Optional<ChecklistProduto> buscarPorId(Long id) {
        return checklistProdutoRepository.findById(id)
                .map(ChecklistProdutoMapper::toDomain);
    }

    @Override
    public List<ChecklistProduto> buscarPorItemChecklist(Long idItemChecklist) {
        return checklistProdutoRepository.findByItemChecklistId(idItemChecklist)
                .stream()
                .map(ChecklistProdutoMapper::toDomain)
                .toList();
    }

    @Override
    public void deletarPorIds(List<Long> ids) {
        checklistProdutoRepository.deleteAllById(ids);
    }
}