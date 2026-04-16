package com.meclist.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.meclist.persistence.entity.VeiculoEntity;

@Repository
public interface VeiculoRepository extends JpaRepository<VeiculoEntity, Long> {
    Optional<VeiculoEntity> findByPlaca(String placa);
    

    @Query("SELECT v FROM VeiculoEntity v WHERE UPPER(v.placa) LIKE UPPER(CONCAT('%', :trecho, '%')) ORDER BY v.placa LIMIT 10")
    List<VeiculoEntity> findTop10ByPlacaContainingIgnoreCase(@Param("trecho") String trecho);

   List<VeiculoEntity> findByClienteId(Long clienteId);


}
