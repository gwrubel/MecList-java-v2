package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.Produto;
import com.meclist.interfaces.ProdutoGateway;
import com.meclist.mapper.ProdutoMapper;
import com.meclist.persistence.entity.ItemChecklistEntity;
import com.meclist.persistence.entity.ProdutoEntity;
import com.meclist.persistence.repository.ItemChecklistRepository;
import com.meclist.persistence.repository.ProdutoRepository;

@Component
public class ProdutoGatewayImpl implements ProdutoGateway {

    private final ProdutoRepository produtoRepository;
    private final ItemChecklistRepository itemChecklistRepository;

    public ProdutoGatewayImpl(ProdutoRepository produtoRepository, ItemChecklistRepository itemChecklistRepository) {
        this.produtoRepository = produtoRepository;
        this.itemChecklistRepository = itemChecklistRepository;
    }

    @Override
    public Produto salvar(Produto produto) {
        ItemChecklistEntity itemChecklist = itemChecklistRepository.findById(produto.getItemChecklist().getId())
                .orElseThrow(() -> new RuntimeException("Item Checklist não encontrado"));
        
        ProdutoEntity entity = ProdutoMapper.toEntity(produto, itemChecklist);
        entity = produtoRepository.save(entity);
        return ProdutoMapper.toDomain(entity);
    }

    @Override
    public List<Produto> buscarPorItemChecklist(Long idItemChecklist) {
        return produtoRepository.findByItemChecklistId(idItemChecklist)
                .stream()
                .map(ProdutoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .map(ProdutoMapper::toDomain);
    }

    @Override
    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }
}