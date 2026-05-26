package com.meclist.persistence.gateway;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.admin.DashboardAdmResponse;
import com.meclist.dto.admin.DashboardEstadoAtualDTO;
import com.meclist.dto.admin.DashboardGraficoDTO;
import com.meclist.dto.admin.DashboardPeriodoDTO;
import com.meclist.dto.admin.DashboardTemposMediosDTO;
import com.meclist.dto.admin.MecanicoTopDTO;
import com.meclist.interfaces.AdminDashboardGateway;
import com.meclist.persistence.repository.ChecklistRepository;
import com.meclist.persistence.repository.OrcamentoRepository;
import com.meclist.persistence.repository.ServicoRepository;

@Component
public class AdminDashboardGatewayImpl implements AdminDashboardGateway {

    private static final List<StatusProcesso> STATUS_PENDENTES = List.of(
            StatusProcesso.INICIADO,
            StatusProcesso.AGUARDANDO_PRECIFICACAO,
            StatusProcesso.AGUARDANDO_APROVACAO,
            StatusProcesso.APROVADO,
            StatusProcesso.ATRIBUIDO,
            StatusProcesso.EM_ANDAMENTO
    );

    private final ServicoRepository servicoRepository;
    private final OrcamentoRepository orcamentoRepository;
    private final ChecklistRepository checklistRepository;

    public AdminDashboardGatewayImpl(ServicoRepository servicoRepository,
                                     OrcamentoRepository orcamentoRepository,
                                     ChecklistRepository checklistRepository) {
        this.servicoRepository = servicoRepository;
        this.orcamentoRepository = orcamentoRepository;
        this.checklistRepository = checklistRepository;
    }

    @Override
    public DashboardAdmResponse buscarDadosDashboard() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicio7Dias = agora.minusDays(7);
        LocalDateTime inicio30Dias = agora.minusDays(30);

        DashboardPeriodoDTO atividade7Dias = montarAtividade("Últimos 7 dias", inicio7Dias);
        DashboardPeriodoDTO atividade30Dias = montarAtividade("Últimos 30 dias", inicio30Dias);
        DashboardEstadoAtualDTO estadoAtual = montarEstadoAtual();

        BigDecimal ticketMedio = orcamentoRepository.calcularTicketMedio(StatusProcesso.CONCLUIDO, inicio30Dias);
        List<MecanicoTopDTO> topMecanicos = buscarTopMecanicos(inicio30Dias);
        DashboardTemposMediosDTO temposMedios = montarTemposMedios(inicio30Dias);

        return new DashboardAdmResponse(
                atividade7Dias,
                atividade30Dias,
                estadoAtual,
                ticketMedio.setScale(2, RoundingMode.HALF_UP),
                topMecanicos,
                temposMedios
        );
    }

    private DashboardPeriodoDTO montarAtividade(String label, LocalDateTime dataInicio) {
        Long movimentados = checklistRepository.contarTotal(dataInicio);
        Long finalizados = checklistRepository.contarPorStatus(StatusProcesso.CONCLUIDO, dataInicio);
        BigDecimal faturamento = orcamentoRepository.somarValorPorStatus(StatusProcesso.CONCLUIDO, dataInicio);
        Double taxaAprovacao = calcularTaxaAprovacao(dataInicio);
        DashboardGraficoDTO grafico = montarGrafico(dataInicio);

        return new DashboardPeriodoDTO(
                label,
                movimentados,
                finalizados,
                faturamento.setScale(2, RoundingMode.HALF_UP),
                taxaAprovacao,
                grafico
        );
    }

    private DashboardEstadoAtualDTO montarEstadoAtual() {
        Long pendentesAtuais = checklistRepository.contarPorStatusesSemData(STATUS_PENDENTES);
        Long aguardandoAprovacao = checklistRepository.contarPorStatusSemData(StatusProcesso.AGUARDANDO_APROVACAO);
        Long atribuidos = servicoRepository.contarPorStatusSemData(StatusProcesso.ATRIBUIDO);
        Long emAndamento = servicoRepository.contarPorStatusSemData(StatusProcesso.EM_ANDAMENTO);
        return new DashboardEstadoAtualDTO(pendentesAtuais, aguardandoAprovacao, atribuidos, emAndamento);
    }

    private DashboardGraficoDTO montarGrafico(LocalDateTime dataInicio) {
        List<Object[]> rows = checklistRepository.contarPorMes(dataInicio);

        List<String> labels = new ArrayList<>();
        List<Long> valores = new ArrayList<>();

        for (Object[] row : rows) {
            int ano = ((Number) row[0]).intValue();
            int mes = ((Number) row[1]).intValue();
            Long quantidade = ((Number) row[2]).longValue();

            String labelMes = Month.of(mes).getDisplayName(TextStyle.SHORT, new Locale("pt", "BR"))
                    + "/" + String.valueOf(ano).substring(2);

            labels.add(labelMes);
            valores.add(quantidade);
        }

        return new DashboardGraficoDTO(labels, valores);
    }

    private Double calcularTaxaAprovacao(LocalDateTime dataInicio) {
        Long aprovados = orcamentoRepository.contarPorStatus(StatusProcesso.APROVADO, dataInicio);
        Long concluidos = orcamentoRepository.contarPorStatus(StatusProcesso.CONCLUIDO, dataInicio);
        Long reprovados = orcamentoRepository.contarPorStatus(StatusProcesso.REPROVADO, dataInicio);

        Long totalDecididos = aprovados + concluidos + reprovados;
        if (totalDecididos == 0) return 0.0;

        return BigDecimal.valueOf((aprovados + concluidos) * 100.0 / totalDecididos)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private List<MecanicoTopDTO> buscarTopMecanicos(LocalDateTime dataInicio) {
        List<Object[]> rows = servicoRepository.buscarTopMecanicos(dataInicio);
        List<MecanicoTopDTO> resultado = new ArrayList<>();

        for (Object[] row : rows) {
            Long id = ((Number) row[0]).longValue();
            String nome = (String) row[1];
            Long quantidade = ((Number) row[2]).longValue();
            resultado.add(new MecanicoTopDTO(id, nome, quantidade));
        }

        return resultado;
    }

    private DashboardTemposMediosDTO montarTemposMedios(LocalDateTime dataInicio) {
        Double ateMecanico = servicoRepository.calcularTempoMedioAteMecanico(dataInicio);
        Double execucao = servicoRepository.calcularTempoMedioExecucao(dataInicio);
        return new DashboardTemposMediosDTO(ateMecanico, execucao);
    }
}
