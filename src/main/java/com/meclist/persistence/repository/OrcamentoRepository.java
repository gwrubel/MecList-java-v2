package com.meclist.persistence.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.meclist.domain.enums.StatusProcesso;
import com.meclist.persistence.entity.OrcamentoEntity;

public interface OrcamentoRepository extends JpaRepository<OrcamentoEntity, Long> {

    Optional<OrcamentoEntity> findByChecklistId(Long checklistId);

    @Query("SELECT COALESCE(SUM(o.valorTotal), 0) FROM OrcamentoEntity o WHERE o.status = :status AND o.atualizadoEm >= :dataInicio")
    BigDecimal somarValorPorStatus(@Param("status") StatusProcesso status,
                                   @Param("dataInicio") LocalDateTime dataInicio);

    @Query("SELECT COALESCE(AVG(o.valorTotal), 0) FROM OrcamentoEntity o WHERE o.status = :status AND o.atualizadoEm >= :dataInicio")
    BigDecimal calcularTicketMedio(@Param("status") StatusProcesso status,
                                   @Param("dataInicio") LocalDateTime dataInicio);

    @Query("SELECT COUNT(o) FROM OrcamentoEntity o WHERE o.status = :status AND o.atualizadoEm >= :dataInicio")
    Long contarPorStatus(@Param("status") StatusProcesso status,
                         @Param("dataInicio") LocalDateTime dataInicio);

    @Query("SELECT COUNT(o) FROM OrcamentoEntity o WHERE o.criadoEm >= :dataInicio AND o.status != 'CANCELADO'")
    Long contarTotal(@Param("dataInicio") LocalDateTime dataInicio);
}
