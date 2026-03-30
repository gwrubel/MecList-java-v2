package com.meclist.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meclist.domain.enums.StatusProcesso;
import com.meclist.persistence.entity.ChecklistEntity;

@Repository
public interface ChecklistRepository extends JpaRepository<ChecklistEntity, Long> {
    
       
    @EntityGraph(attributePaths = {
        "veiculo",
        "veiculo.cliente",
        "itensChecklist",           // ← Nome correto!
        "itensChecklist.item"       // ← Carrega os itens também
    })
    List<ChecklistEntity> findByMecanicoId(Long mecanicoId);
    
    @EntityGraph(attributePaths = {
        "veiculo", 
        "veiculo.cliente",
        "itensChecklist",
        "itensChecklist.item"
    })
    List<ChecklistEntity> findByVeiculoId(Long veiculoId);

    @EntityGraph(attributePaths = {
        "veiculo", 
        "veiculo.cliente",
        "itensChecklist",
        "itensChecklist.item"
    })
    Optional<ChecklistEntity> findById(Long id);

    List<ChecklistEntity> findByStatus(StatusProcesso status);
}



