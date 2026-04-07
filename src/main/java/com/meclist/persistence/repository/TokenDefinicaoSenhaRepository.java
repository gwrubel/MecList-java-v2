package com.meclist.persistence.repository;

import com.meclist.persistence.entity.TokenDefinicaoSenhaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenDefinicaoSenhaRepository extends JpaRepository<TokenDefinicaoSenhaEntity, Long> {
    Optional<TokenDefinicaoSenhaEntity> findByToken(String token);
    Optional<TokenDefinicaoSenhaEntity> findFirstByEmailAndUsadoFalseOrderByCriadoEmDesc(String email);
}
