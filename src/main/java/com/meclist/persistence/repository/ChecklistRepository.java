package com.meclist.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.meclist.domain.enums.StatusProcesso;
import com.meclist.persistence.entity.ChecklistEntity;

@Repository
public interface ChecklistRepository extends JpaRepository<ChecklistEntity, Long> {

    @EntityGraph(attributePaths = {
            "veiculo",
            "veiculo.cliente",
            "itensChecklist", // ← Nome correto!
            "itensChecklist.item" // ← Carrega os itens também
    })
    List<ChecklistEntity> findByMecanicoId(Long mecanicoId);

    @EntityGraph(attributePaths = {
            "veiculo",
            "veiculo.cliente",
            "itensChecklist",
            "itensChecklist.item"
    })
    List<ChecklistEntity> findByVeiculoId(Long veiculoId);

    @EntityGraph(attributePaths = {
            "veiculo",
            "veiculo.cliente",
            "itensChecklist",
            "itensChecklist.item"
    })
    Optional<ChecklistEntity> findById(Long id);

    @EntityGraph(attributePaths = {
            "veiculo",
            "veiculo.cliente"
    })
    List<ChecklistEntity> findByStatus(StatusProcesso status);

    @Query("""
                SELECT c FROM ChecklistEntity c
                WHERE c.veiculo.cliente.id = :clienteId
                AND (
                    c.status IN :statusAtivos
                    OR (c.status IN :statusComLimite AND c.atualizadoEm >= :limiteData)
                )
            """)
    @EntityGraph(attributePaths = { "veiculo", "veiculo.cliente", "mecanico" })
    List<ChecklistEntity> findDashboardPorCliente(
            @Param("clienteId") Long clienteId,
            @Param("statusAtivos") List<StatusProcesso> statusAtivos,
            @Param("statusComLimite") List<StatusProcesso> statusComLimite,
            @Param("limiteData") LocalDateTime limiteData);

    @Query("SELECT c FROM ChecklistEntity c WHERE c.status = 'INICIADO' AND c.criadoEm < :limite")
    List<ChecklistEntity> findInativosParaCancelamento(@Param("limite") LocalDateTime limite);

    @Query("SELECT COUNT(c) FROM ChecklistEntity c WHERE c.atualizadoEm >= :dataInicio AND c.status != 'CANCELADO'")
    Long contarTotal(@Param("dataInicio") LocalDateTime dataInicio);

    @Query("SELECT COUNT(c) FROM ChecklistEntity c WHERE c.status IN :statuses")
    Long contarPorStatusesSemData(@Param("statuses") List<StatusProcesso> statuses);

    @Query("SELECT COUNT(c) FROM ChecklistEntity c WHERE c.status = :status")
    Long contarPorStatusSemData(@Param("status") StatusProcesso status);

    @Query("SELECT COUNT(c) FROM ChecklistEntity c WHERE c.status = :status AND c.atualizadoEm >= :dataInicio")
    Long contarPorStatus(@Param("status") StatusProcesso status,
                         @Param("dataInicio") LocalDateTime dataInicio);

    @Query("SELECT COUNT(c) FROM ChecklistEntity c WHERE c.status IN :statuses AND c.criadoEm >= :dataInicio")
    Long contarPorStatuses(@Param("statuses") List<StatusProcesso> statuses,
                           @Param("dataInicio") LocalDateTime dataInicio);

    @Query("SELECT YEAR(c.atualizadoEm), MONTH(c.atualizadoEm), COUNT(c) " +
           "FROM ChecklistEntity c " +
           "WHERE c.atualizadoEm >= :dataInicio AND c.status != 'CANCELADO'" +
           "GROUP BY YEAR(c.atualizadoEm), MONTH(c.atualizadoEm) " +
           "ORDER BY YEAR(c.atualizadoEm), MONTH(c.atualizadoEm)")
    List<Object[]> contarPorMes(@Param("dataInicio") LocalDateTime dataInicio);
}
