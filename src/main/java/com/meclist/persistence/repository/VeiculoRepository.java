package com.meclist.persistence.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meclist.persistence.entity.VeiculoEntity;

@Repository
public interface VeiculoRepository extends JpaRepository<VeiculoEntity, Long> {
    Optional<VeiculoEntity> findByPlaca(String placa);
    List<VeiculoEntity> findTop10ByPlacaContainingIgnoreCase(String placa);

}
