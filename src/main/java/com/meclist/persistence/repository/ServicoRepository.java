package com.meclist.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.meclist.domain.enums.StatusProcesso;
import com.meclist.persistence.entity.ServicoEntity;

@Repository
public interface ServicoRepository extends JpaRepository<ServicoEntity, Long> {
    
    @Query("SELECT s FROM ServicoEntity s " +
           "WHERE s.checklist.veiculo.id = :veiculoId " +
           "AND s.status = :status " +
           "ORDER BY s.dataRealizacao DESC, s.id DESC " +
           "LIMIT 1")
    Optional<ServicoEntity> encontrarUltimaRevisao(@Param("veiculoId") Long veiculoId,
                                                           @Param("status") StatusProcesso status);
}
