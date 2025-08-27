package com.meclist.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.persistence.entity.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    
    @Query("SELECT i FROM ItemEntity i WHERE i.parteDoVeiculo = :categoria")
    List<ItemEntity> findByCategoria(@Param("categoria") CategoriaParteVeiculo categoria);
}
