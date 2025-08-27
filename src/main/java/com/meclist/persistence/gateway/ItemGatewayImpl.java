package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.Item;
import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.interfaces.ItemGateway;
import com.meclist.mapper.ItemMapper;
import com.meclist.persistence.entity.ItemEntity;
import com.meclist.persistence.repository.ItemRepository;

@Component
public class ItemGatewayImpl implements ItemGateway {

    private final ItemRepository itemRepository;

    public ItemGatewayImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item salvar(Item item) {
        ItemEntity entity = ItemMapper.toEntity(item);
        ItemEntity salvo = itemRepository.save(entity);
        return ItemMapper.toDomain(salvo);
    }

    @Override
    public List<Item> buscarPorCategoria(CategoriaParteVeiculo categoria) {
        return itemRepository.findByCategoria(categoria)
                .stream()
                .map(ItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> buscarPorId(Long id) {
        return itemRepository.findById(id).map(ItemMapper::toDomain);
    }
    
    @Override
    public List<Item> buscarTodos() {
        return itemRepository.findAll()
                .stream()
                .map(ItemMapper::toDomain)
                .collect(Collectors.toList());
    }
}
