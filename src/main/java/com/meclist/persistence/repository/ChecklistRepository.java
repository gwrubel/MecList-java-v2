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
                    OR (c.status = 'CONCLUIDO' AND c.atualizadoEm >= :limiteData)
                )
            """)
    @EntityGraph(attributePaths = { "veiculo", "veiculo.cliente", "mecanico" })
    List<ChecklistEntity> findDashboardPorCliente(
            @Param("clienteId") Long clienteId,
            @Param("statusAtivos") List<StatusProcesso> statusAtivos,
            @Param("limiteData") LocalDateTime limiteData);
}
