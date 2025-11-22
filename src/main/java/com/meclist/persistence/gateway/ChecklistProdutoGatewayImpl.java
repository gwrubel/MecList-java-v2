package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.ChecklistProduto;
import com.meclist.interfaces.ChecklistProdutoGateway;
import com.meclist.mapper.ChecklistProdutoMapper;
import com.meclist.persistence.entity.ChecklistProdutoEntity;
import com.meclist.persistence.entity.ItemChecklistEntity;
import com.meclist.persistence.entity.ProdutoEntity;
import com.meclist.persistence.repository.ChecklistProdutoRepository;
import com.meclist.persistence.repository.ItemChecklistRepository;
import com.meclist.persistence.repository.ProdutoRepository;

@Component
public class ChecklistProdutoGatewayImpl implements ChecklistProdutoGateway {

    private final ChecklistProdutoRepository checklistProdutoRepository;
    private final ItemChecklistRepository itemChecklistRepository;
    private final ProdutoRepository produtoRepository;

    public ChecklistProdutoGatewayImpl(ChecklistProdutoRepository checklistProdutoRepository,
                                      ItemChecklistRepository itemChecklistRepository,
                                      ProdutoRepository produtoRepository) {
        this.checklistProdutoRepository = checklistProdutoRepository;
        this.itemChecklistRepository = itemChecklistRepository;
        this.produtoRepository = produtoRepository;
    }

    @Override
    public ChecklistProduto salvar(ChecklistProduto checklistProduto) {
        ItemChecklistEntity itemChecklistEntity = itemChecklistRepository.findById(
                checklistProduto.getItemChecklist().getId())
                .orElseThrow(() -> new RuntimeException("Item Checklist não encontrado"));
        
        ProdutoEntity produtoEntity = produtoRepository.findById(checklistProduto.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        ChecklistProdutoEntity entity = ChecklistProdutoMapper.toEntity(
                checklistProduto, itemChecklistEntity, produtoEntity);
        entity = checklistProdutoRepository.save(entity);
        return ChecklistProdutoMapper.toDomain(entity);
    }

    @Override
    public List<ChecklistProduto> buscarPorItemChecklist(Long idItemChecklist) {
        return checklistProdutoRepository.findByItemChecklistId(idItemChecklist)
                .stream()
                .map(ChecklistProdutoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChecklistProduto> buscarPorItemChecklistEAprovacao(Long idItemChecklist, Boolean aprovado) {
        return checklistProdutoRepository.findByItemChecklistIdAndAprovadoCliente(idItemChecklist, aprovado)
                .stream()
                .map(ChecklistProdutoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChecklistProduto> buscarPendentesPorItemChecklist(Long idItemChecklist) {
        return checklistProdutoRepository.findByItemChecklistIdAndAprovadoClientePendente(idItemChecklist)
                .stream()
                .map(ChecklistProdutoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ChecklistProduto> buscarPorId(Long id) {
        return checklistProdutoRepository.findById(id)
                .map(ChecklistProdutoMapper::toDomain);
    }

    @Override
    public void excluir(Long id) {
        checklistProdutoRepository.deleteById(id);
    }
}

