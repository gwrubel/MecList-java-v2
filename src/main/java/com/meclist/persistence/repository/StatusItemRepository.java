package com.meclist.persistence.repository;

import com.meclist.persistence.entity.StatusItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusItemRepository extends JpaRepository<StatusItemEntity, Long> {
    // Método para buscar o status padrão pela descrição
    Optional<StatusItemEntity> findByDescricao(String descricao);
}