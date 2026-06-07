package com.meclist.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.Situacao;
import com.meclist.persistence.entity.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    
    @Query("SELECT i FROM ItemEntity i WHERE i.parteDoVeiculo = :categoria")
    List<ItemEntity> findByCategoria(@Param("categoria") CategoriaParteVeiculo categoria);

    List<ItemEntity> findBySituacao(Situacao situacao);

        @Query("""
            SELECT COUNT(i) > 0
            FROM ItemEntity i
            WHERE i.nomeItem = :nomeItem
              AND i.parteDoVeiculo = :categoria
              AND (:idIgnorar IS NULL OR i.id <> :idIgnorar)
            """)
        boolean existsDuplicado(@Param("nomeItem") String nomeItem,
            @Param("categoria") CategoriaParteVeiculo categoria,
            @Param("idIgnorar") Long idIgnorar);
}
