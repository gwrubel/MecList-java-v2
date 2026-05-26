package com.meclist.persistence.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.meclist.domain.enums.StatusProcesso;
import com.meclist.persistence.entity.ServicoEntity;

@Repository
public interface ServicoRepository extends JpaRepository<ServicoEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {
           "checklist",
           "checklist.veiculo",
           "mecanico"
    })
    Optional<ServicoEntity> findById(Long id);

    List<ServicoEntity> findByChecklistIdOrderByIdDesc(Long checklistId);

    Optional<ServicoEntity> findFirstByChecklistIdAndStatus(Long checklistId, StatusProcesso status);

    @EntityGraph(attributePaths = {
           "checklist",
           "checklist.veiculo",
           "mecanico"
    })
    List<ServicoEntity> findByMecanicoIdAndStatusInOrderByAtualizadoEmDescIdDesc(Long mecanicoId,
                                                                         Collection<StatusProcesso> statuses);

    @Query("SELECT s FROM ServicoEntity s " +
           "WHERE s.checklist.veiculo.id = :veiculoId " +
           "AND s.status = :status " +
           "ORDER BY s.dataConclusao DESC, s.id DESC " +
           "LIMIT 1")
    Optional<ServicoEntity> encontrarUltimaRevisao(@Param("veiculoId") Long veiculoId,
                                                   @Param("status") StatusProcesso status);

    @Query("SELECT COUNT(s) FROM ServicoEntity s WHERE s.criadoEm >= :dataInicio")
    Long contarTotal(@Param("dataInicio") java.time.LocalDateTime dataInicio);

    @Query("SELECT COUNT(s) FROM ServicoEntity s WHERE s.status = :status AND s.criadoEm >= :dataInicio")
    Long contarPorStatus(@Param("status") StatusProcesso status,
                         @Param("dataInicio") java.time.LocalDateTime dataInicio);

    @Query("SELECT COUNT(s) FROM ServicoEntity s WHERE s.status IN :statuses AND s.criadoEm >= :dataInicio")
    Long contarPorStatuses(@Param("statuses") List<StatusProcesso> statuses,
                           @Param("dataInicio") java.time.LocalDateTime dataInicio);

    // Retorna List<Object[]> onde [0]=ano, [1]=mes, [2]=quantidade
    @Query("SELECT YEAR(s.criadoEm), MONTH(s.criadoEm), COUNT(s) " +
           "FROM ServicoEntity s " +
           "WHERE s.criadoEm >= :dataInicio " +
           "GROUP BY YEAR(s.criadoEm), MONTH(s.criadoEm) " +
           "ORDER BY YEAR(s.criadoEm), MONTH(s.criadoEm)")
    List<Object[]> contarPorMes(@Param("dataInicio") java.time.LocalDateTime dataInicio);

    // Retorna List<Object[]> onde [0]=mecanicoId, [1]=nome, [2]=quantidade
    @Query("SELECT s.mecanico.id, s.mecanico.nome, COUNT(s) " +
           "FROM ServicoEntity s " +
           "WHERE s.criadoEm >= :dataInicio " +
           "GROUP BY s.mecanico.id, s.mecanico.nome " +
           "ORDER BY COUNT(s) DESC " +
           "LIMIT 3")
    List<Object[]> buscarTopMecanicos(@Param("dataInicio") java.time.LocalDateTime dataInicio);

    @EntityGraph(attributePaths = {
           "checklist",
           "checklist.veiculo",
           "checklist.veiculo.cliente",
           "mecanico"
    })
    List<ServicoEntity> findByMecanicoIdAndStatusOrderByDataConclusaoDescIdDesc(
            Long mecanicoId, StatusProcesso status);

    @Query(value = """
            SELECT ROUND(AVG(EXTRACT(EPOCH FROM (data_inicio - data_atribuicao)) / 60.0)::numeric, 1)
            FROM servico
            WHERE status = 'CONCLUIDO'
              AND data_conclusao >= :dataInicio
              AND data_inicio IS NOT NULL
              AND data_atribuicao IS NOT NULL
            """, nativeQuery = true)
    Double calcularTempoMedioAteMecanico(@Param("dataInicio") java.time.LocalDateTime dataInicio);

    @Query(value = """
            SELECT ROUND(AVG(EXTRACT(EPOCH FROM (data_conclusao - data_inicio)) / 60.0)::numeric, 1)
            FROM servico
            WHERE status = 'CONCLUIDO'
              AND data_conclusao >= :dataInicio
              AND data_inicio IS NOT NULL
              AND data_conclusao IS NOT NULL
            """, nativeQuery = true)
    Double calcularTempoMedioExecucao(@Param("dataInicio") java.time.LocalDateTime dataInicio);

    @Query("SELECT COUNT(s) FROM ServicoEntity s WHERE s.status = :status")
    Long contarPorStatusSemData(@Param("status") StatusProcesso status);
}
