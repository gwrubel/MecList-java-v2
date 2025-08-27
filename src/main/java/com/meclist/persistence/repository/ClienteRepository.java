package com.meclist.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meclist.domain.enums.Situacao;
import com.meclist.persistence.entity.ClienteEntity;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    Optional<ClienteEntity> findByEmail(String email);
    Optional<ClienteEntity> findByDocumento(String documento);
    List<ClienteEntity> findBySituacao(Situacao situacao);
    
    
    
}
