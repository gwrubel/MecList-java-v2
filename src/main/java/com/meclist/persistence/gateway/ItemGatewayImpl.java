package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.Item;
import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.interfaces.ItemGateway;
import com.meclist.mapper.ItemMapper;
import com.meclist.mapper.ProdutoMapper;
import com.meclist.persistence.entity.ItemEntity;
import com.meclist.persistence.repository.ItemRepository;
import com.meclist.persistence.repository.ItemProdutoRepository;

@Component
public class ItemGatewayImpl implements ItemGateway {

    private final ItemRepository itemRepository;
    private final ItemProdutoRepository itemProdutoRepository;

    public ItemGatewayImpl(ItemRepository itemRepository, ItemProdutoRepository itemProdutoRepository) {
        this.itemRepository = itemRepository;
        this.itemProdutoRepository = itemProdutoRepository;
    }

    @Override
    public Item salvar(Item item) {
        ItemEntity entity;
        if (item.getId() != null) {
            entity = itemRepository.findById(item.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Item não encontrado: " + item.getId()));
        } else {
            entity = new ItemEntity();
            entity.setCriadoEm(item.getCriadoEm());
        }

        entity.setNomeItem(item.getNome());
        entity.setParteDoVeiculo(item.getParteDoVeiculo());
        entity.setImagemIlustrativa(item.getImagemIlustrativa());
        entity.setAtualizadoEm(item.getAtualizadoEm());

        ItemEntity salvo = itemRepository.save(entity);
        return ItemMapper.toDomain(salvo);
    }

        @Override
    public List<Item> buscarPorCategoria(CategoriaParteVeiculo categoria) {
        return itemRepository.findByCategoria(categoria)
                .stream()
                .map(entity -> {
                    var produtos = itemProdutoRepository.findByItemId(entity.getId())
                        .stream()
                        .map(ip -> ProdutoMapper.toDomain(ip.getProduto()))
                        .collect(Collectors.toList());

                
                    
                    return ItemMapper.ItemComProdutosToDomain(entity, produtos);
                })
                .collect(Collectors.toList());
    }

     @Override
    public Optional<Item> buscarPorId(Long id) {
        Optional<ItemEntity> itemEntity = itemRepository.findById(id);
        if (itemEntity.isEmpty()) {
            return Optional.empty();
        }
        
        // Carrega os produtos associados
        var produtos = itemProdutoRepository.findByItemId(itemEntity.get().getId())
            .stream()
            .map(ip -> ProdutoMapper.toDomain(ip.getProduto()))
            .collect(Collectors.toList());
        
        return Optional.of(ItemMapper.ItemComProdutosToDomain(itemEntity.get(), produtos));
    }
    
      @Override
    public List<Item> buscarTodos() {
        return itemRepository.findAll()
                .stream()
                .map(entity -> {
                    var produtos = itemProdutoRepository.findByItemId(entity.getId())
                        .stream()
                        .map(ip -> ProdutoMapper.toDomain(ip.getProduto()))
                        .collect(Collectors.toList());

                        System.out.println("produtos "+ produtos);
                    return ItemMapper.ItemComProdutosToDomain(entity, produtos);
                })
                .collect(Collectors.toList());
    }
    @Override
    public boolean existeComMesmoNome(String nome) {
        return itemRepository.existsByNomeItem(nome);
    }

    @Override
    public void excluir (Long id) {
        itemRepository.deleteById(id);
    }
}
