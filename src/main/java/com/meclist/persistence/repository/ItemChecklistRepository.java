package com.meclist.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.meclist.persistence.entity.ItemChecklistEntity;

public interface ItemChecklistRepository extends JpaRepository<ItemChecklistEntity, Long> {
    @Query("SELECT ic FROM ItemChecklistEntity ic WHERE ic.checklist.id = :checklistId")
    List<ItemChecklistEntity> findByChecklist(@Param("checklistId") Long checklistId);
}



