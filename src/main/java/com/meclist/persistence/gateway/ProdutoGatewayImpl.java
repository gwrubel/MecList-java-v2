package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.Produto;
import com.meclist.interfaces.ProdutoGateway;
import com.meclist.mapper.ProdutoMapper;
import com.meclist.persistence.entity.ProdutoEntity;
import com.meclist.persistence.repository.ProdutoRepository;

@Component
public class ProdutoGatewayImpl implements ProdutoGateway {

    private final ProdutoRepository produtoRepository;

    public ProdutoGatewayImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public Produto salvar(Produto produto) {
        ProdutoEntity entity = ProdutoMapper.toEntity(produto);
        entity = produtoRepository.save(entity);
        return ProdutoMapper.toDomain(entity);
    }

    @Override
    public List<Produto> buscarTodos() {
        return produtoRepository.findAll()
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
    public Optional<Produto> buscarPorNome(String nomeProduto) {
        return produtoRepository.findByNomeProduto(nomeProduto)
                .map(ProdutoMapper::toDomain);
    }

    @Override
    public List<Produto> buscarPorNomeContendo(String nomeProduto) {
        return produtoRepository.findByNomeProdutoContainingIgnoreCase(nomeProduto)
                .stream()
                .map(ProdutoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }

    @Override
    public Produto atualizar(Produto produto) {
        ProdutoEntity entity = ProdutoMapper.toEntity(produto);
        entity = produtoRepository.save(entity);
        return ProdutoMapper.toDomain(entity);
    }
}