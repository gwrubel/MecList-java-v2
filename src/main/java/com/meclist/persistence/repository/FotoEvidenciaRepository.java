package com.meclist.persistence.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meclist.persistence.entity.FotoEvidenciaEntity;

@Repository
public interface FotoEvidenciaRepository extends JpaRepository<FotoEvidenciaEntity, Long> {
    
    
    List<FotoEvidenciaEntity> findByItemChecklistId(Long itemChecklistId);
    
    void deleteByItemChecklistId(Long itemChecklistId);
}
