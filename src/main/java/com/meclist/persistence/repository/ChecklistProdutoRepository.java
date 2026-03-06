package com.meclist.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.meclist.persistence.entity.ChecklistProdutoEntity;

public interface ChecklistProdutoRepository extends JpaRepository<ChecklistProdutoEntity, Long> {

    List<ChecklistProdutoEntity> findByItemChecklistId(Long itemChecklistId);
    
}

