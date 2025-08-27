package com.meclist.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meclist.domain.enums.Situacao;
import com.meclist.persistence.entity.MecanicoEntity;

@Repository
public interface MecanicoRepository extends JpaRepository<MecanicoEntity, Long> {
    
    Optional<MecanicoEntity> findByEmail(String email);
    Optional<MecanicoEntity> findByCpf(String cpf);
    List<MecanicoEntity> findBySituacao(Situacao situacao);

    
    
}
