package com.meclist.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meclist.persistence.entity.OrcamentoEntity;

public interface OrcamentoRepository extends JpaRepository<OrcamentoEntity, Long> {
    Optional<OrcamentoEntity> findByChecklistId(Long checklistId);
}
