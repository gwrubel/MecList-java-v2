package com.meclist.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.meclist.persistence.entity.ItemChecklistEntity;

public interface ItemChecklistRepository extends JpaRepository<ItemChecklistEntity, Long> {

    // ✅ Busca um item específico com suas relações
    @EntityGraph(attributePaths = {"checklist", "item"})
    @Query("SELECT ic FROM ItemChecklistEntity ic " +
           "WHERE ic.checklist.id = :checklistId " +
           "AND ic.item.id = :itemId")
    Optional<ItemChecklistEntity> findByChecklistIdAndItemId(
        @Param("checklistId") Long checklistId, 
        @Param("itemId") Long itemId);

    // ✅ Lista todos os itens de um checklist (método único)
    @EntityGraph(attributePaths = {"checklist", "item"})
    List<ItemChecklistEntity> findByChecklistId(Long checklistId);

    // ✅ Busca por ID com todas as relações
    @EntityGraph(attributePaths = {"checklist", "item"})
    Optional<ItemChecklistEntity> findById(Long id);
}



