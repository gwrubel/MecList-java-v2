package com.meclist.persistence.gateway;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.ItemProduto;
import com.meclist.interfaces.ItemProdutoGateway;
import com.meclist.mapper.ItemProdutoMapper;
import com.meclist.persistence.entity.ItemEntity;
import com.meclist.persistence.entity.ItemProdutoEntity;
import com.meclist.persistence.entity.ProdutoEntity;
import com.meclist.persistence.repository.ItemProdutoRepository;
import com.meclist.persistence.repository.ItemRepository;
import com.meclist.persistence.repository.ProdutoRepository;

@Component
public class ItemProdutoGatewayImpl implements ItemProdutoGateway {

    private final ItemProdutoRepository itemProdutoRepository;
    private final ItemRepository itemRepository;
    private final ProdutoRepository produtoRepository;

    public ItemProdutoGatewayImpl(ItemProdutoRepository itemProdutoRepository,
                                 ItemRepository itemRepository,
                                 ProdutoRepository produtoRepository) {
        this.itemProdutoRepository = itemProdutoRepository;
        this.itemRepository = itemRepository;
        this.produtoRepository = produtoRepository;
    }

    @Override
    public ItemProduto salvar(ItemProduto itemProduto) {
        ItemEntity itemEntity = itemRepository.getReferenceById(
            itemProduto.getItem().getId()
        );
        
        ProdutoEntity produtoEntity = produtoRepository.getReferenceById(
            itemProduto.getProduto().getId()
        );
        
        ItemProdutoEntity entity = ItemProdutoMapper.toEntity(itemProduto, itemEntity, produtoEntity);
        entity = itemProdutoRepository.save(entity);
        return ItemProdutoMapper.toDomain(entity);
    }

    @Override
    public List<ItemProduto> buscarPorItem(Long idItem) {
        return itemProdutoRepository.findByItemId(idItem)
                .stream()
                .map(ItemProdutoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemProduto> buscarPorProduto(Long idProduto) {
        return itemProdutoRepository.findByProdutoId(idProduto)
                .stream()
                .map(ItemProdutoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeRelacionamento(Long idItem, Long idProduto) {
        return itemProdutoRepository.existsByItemIdAndProdutoId(idItem, idProduto);
    }

    @Override
    public void excluir(Long id) {
        itemProdutoRepository.deleteById(id);
    }

    @Override
    public void excluirPorItemEProduto(Long idItem, Long idProduto) {
        itemProdutoRepository.findByItemIdAndProdutoId(idItem, idProduto)
                .ifPresent(itemProdutoRepository::delete);
    }
}

