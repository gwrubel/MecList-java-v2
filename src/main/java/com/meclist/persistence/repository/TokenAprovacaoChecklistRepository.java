package com.meclist.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meclist.persistence.entity.TokenAprovacaoChecklistEntity;

public interface TokenAprovacaoChecklistRepository extends JpaRepository<TokenAprovacaoChecklistEntity, Long> {
    Optional<TokenAprovacaoChecklistEntity> findByToken(String token);
}