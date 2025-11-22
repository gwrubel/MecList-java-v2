package com.meclist.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meclist.persistence.entity.ItemProdutoEntity;

@Repository
public interface ItemProdutoRepository extends JpaRepository<ItemProdutoEntity, Long> {
    List<ItemProdutoEntity> findByItemId(Long idItem);
    List<ItemProdutoEntity> findByProdutoId(Long idProduto);
    Optional<ItemProdutoEntity> findByItemIdAndProdutoId(Long idItem, Long idProduto);
    boolean existsByItemIdAndProdutoId(Long idItem, Long idProduto);
}

