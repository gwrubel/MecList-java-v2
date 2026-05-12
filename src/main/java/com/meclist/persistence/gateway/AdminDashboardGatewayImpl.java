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
import com.meclist.dto.admin.DashboardGraficoDTO;
import com.meclist.dto.admin.DashboardPeriodoDTO;
import com.meclist.dto.admin.MecanicoTopDTO;
import com.meclist.interfaces.AdminDashboardGateway;
import com.meclist.persistence.repository.OrcamentoRepository;
import com.meclist.persistence.repository.ServicoRepository;

@Component
public class AdminDashboardGatewayImpl implements AdminDashboardGateway {

    private static final List<StatusProcesso> STATUSES_PENDENTES = List.of(
            StatusProcesso.INICIADO,
            StatusProcesso.EM_ANDAMENTO,
            StatusProcesso.AGUARDANDO_PRECIFICACAO,
            StatusProcesso.AGUARDANDO_APROVACAO,
            StatusProcesso.APROVADO
    );

    private final ServicoRepository servicoRepository;
    private final OrcamentoRepository orcamentoRepository;

    public AdminDashboardGatewayImpl(ServicoRepository servicoRepository,
                                     OrcamentoRepository orcamentoRepository) {
        this.servicoRepository = servicoRepository;
        this.orcamentoRepository = orcamentoRepository;
    }

    @Override
    public DashboardAdmResponse buscarDadosDashboard() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicio7Dias = agora.minusDays(7);
        LocalDateTime inicio30Dias = agora.minusDays(30);

        DashboardPeriodoDTO periodo7Dias = montarPeriodo("Últimos 7 dias", inicio7Dias);
        DashboardPeriodoDTO periodo30Dias = montarPeriodo("Últimos 30 dias", inicio30Dias);

        BigDecimal ticketMedio = orcamentoRepository.calcularTicketMedio(StatusProcesso.CONCLUIDO, inicio30Dias);
        Double taxaAprovacao = calcularTaxaAprovacao(inicio30Dias);
        List<MecanicoTopDTO> topMecanicos = buscarTopMecanicos(inicio30Dias);

        return new DashboardAdmResponse(
                periodo7Dias,
                periodo30Dias,
                ticketMedio.setScale(2, RoundingMode.HALF_UP),
                taxaAprovacao,
                topMecanicos
        );
    }

    private DashboardPeriodoDTO montarPeriodo(String label, LocalDateTime dataInicio) {
        Long total = servicoRepository.contarTotal(dataInicio);
        Long pendentes = servicoRepository.contarPorStatuses(STATUSES_PENDENTES, dataInicio);
        Long finalizados = servicoRepository.contarPorStatus(StatusProcesso.CONCLUIDO, dataInicio);
        BigDecimal valorTotal = orcamentoRepository.somarValorPorStatus(StatusProcesso.CONCLUIDO, dataInicio);
        DashboardGraficoDTO grafico = montarGrafico(dataInicio);

        return new DashboardPeriodoDTO(
                label,
                total,
                pendentes,
                finalizados,
                valorTotal.setScale(2, RoundingMode.HALF_UP),
                grafico
        );
    }

    private DashboardGraficoDTO montarGrafico(LocalDateTime dataInicio) {
        List<Object[]> rows = servicoRepository.contarPorMes(dataInicio);

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
        Long total = orcamentoRepository.contarTotal(dataInicio);
        if (total == 0) return 0.0;

        Long aprovados = orcamentoRepository.contarPorStatus(StatusProcesso.APROVADO, dataInicio);
        Long concluidos = orcamentoRepository.contarPorStatus(StatusProcesso.CONCLUIDO, dataInicio);
        Long aprovadosTotal = aprovados + concluidos;

        return BigDecimal.valueOf(aprovadosTotal * 100.0 / total)
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
}
