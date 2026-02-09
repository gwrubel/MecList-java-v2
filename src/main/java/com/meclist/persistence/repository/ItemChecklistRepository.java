package com.meclist.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meclist.persistence.entity.ItemChecklistEntity;

@Repository
public interface ItemChecklistRepository extends JpaRepository<ItemChecklistEntity, Long> {
    
    List<ItemChecklistEntity> findByChecklistId(Long checklistId);
}



