package com.meclist.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meclist.persistence.entity.ChecklistEntity;

@Repository
public interface ChecklistRepository extends JpaRepository<ChecklistEntity, Long> {
    
    List<ChecklistEntity> findByMecanicoId(Long mecanicoId);
    
    List<ChecklistEntity> findByVeiculoId(Long veiculoId);
}



