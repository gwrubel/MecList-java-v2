package com.meclist.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.meclist.persistence.entity.ChecklistProdutoEntity;

@Repository
public interface ChecklistProdutoRepository extends JpaRepository<ChecklistProdutoEntity, Long> {
    List<ChecklistProdutoEntity> findByItemChecklistId(Long idItemChecklist);
    
    @Query("SELECT cp FROM ChecklistProdutoEntity cp WHERE cp.itemChecklist.id = :idItemChecklist AND cp.aprovadoCliente = :aprovado")
    List<ChecklistProdutoEntity> findByItemChecklistIdAndAprovadoCliente(
        @Param("idItemChecklist") Long idItemChecklist, 
        @Param("aprovado") Boolean aprovado
    );
    
    @Query("SELECT cp FROM ChecklistProdutoEntity cp WHERE cp.itemChecklist.id = :idItemChecklist AND cp.aprovadoCliente IS NULL")
    List<ChecklistProdutoEntity> findByItemChecklistIdAndAprovadoClientePendente(@Param("idItemChecklist") Long idItemChecklist);
}

