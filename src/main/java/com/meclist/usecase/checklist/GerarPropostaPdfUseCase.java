package com.meclist.usecase.checklist;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Checklist;
import com.meclist.domain.ChecklistProduto;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.enums.StatusItem;
import com.meclist.dto.checklist.administrativo.PropostaPdfItemSnapshot;
import com.meclist.dto.checklist.administrativo.PropostaPdfProdutoSnapshot;
import com.meclist.dto.checklist.administrativo.PropostaPdfSnapshot;
import com.meclist.interfaces.OrcamentoGateway;
import com.meclist.interfaces.PropostaPdfGateway;
import com.meclist.security.AuthenticatedUser;

@Service
public class GerarPropostaPdfUseCase {

    private final BuscarChecklistOrcamentoParaAprovacaoService buscarChecklistOrcamentoParaAprovacaoService;
    private final ChecklistWorkflowGuard workflowGuard;
    private final PropostaPdfGateway propostaPdfGateway;
    private final OrcamentoGateway orcamentoGateway;

    public GerarPropostaPdfUseCase(
            BuscarChecklistOrcamentoParaAprovacaoService buscarChecklistOrcamentoParaAprovacaoService,
            ChecklistWorkflowGuard workflowGuard,
            PropostaPdfGateway propostaPdfGateway,
            OrcamentoGateway orcamentoGateway) {
        this.buscarChecklistOrcamentoParaAprovacaoService = buscarChecklistOrcamentoParaAprovacaoService;
        this.workflowGuard = workflowGuard;
        this.propostaPdfGateway = propostaPdfGateway;
        this.orcamentoGateway = orcamentoGateway;
    }

    @Transactional
    public byte[] executar(Long checklistId) {
        ChecklistOrcamentoContext context = buscarChecklistOrcamentoParaAprovacaoService.carregar(checklistId);
        workflowGuard.validarGeracaoPdfManualPorAdm(context.checklist(), context.orcamento());
        AuthenticatedUser user = workflowGuard.obterUsuarioAutenticado();

        PropostaPdfSnapshot snapshot = criarSnapshot(context.checklist(), context.orcamento());
        byte[] pdf = propostaPdfGateway.gerar(snapshot);

        context.orcamento().registrarGeracaoPdf(user.id());
        orcamentoGateway.salvar(context.orcamento());

        return pdf;
    }

    private PropostaPdfSnapshot criarSnapshot(com.meclist.domain.Checklist checklist,
                                              com.meclist.domain.Orcamento orcamento) {
        List<PropostaPdfItemSnapshot> itens = checklist.getItensChecklist().stream()
                .filter(item -> item.getStatusItem() == StatusItem.TROCAR)
                .sorted(Comparator.comparing(item -> item.getItem().getParteDoVeiculo().name()))
                .map(this::toItemSnapshot)
                .toList();

        return new PropostaPdfSnapshot(
                checklist.getId(),
                checklist.getVeiculo().getCliente().getNome(),
                checklist.getVeiculo().getCliente().getDocumento(),
                checklist.getVeiculo().getCliente().getTelefone(),
                checklist.getVeiculo().getCliente().getEndereco(),
                checklist.getVeiculo().getPlaca(),
                checklist.getVeiculo().getMarca(),
                checklist.getVeiculo().getModelo(),
                checklist.getVeiculo().getAno(),
                checklist.getQuilometragem(),
                orcamento.getDataEmissao(),
                checklist.calcularTotalMaoDeObra(),
                checklist.calcularTotalProdutos(),
                checklist.calcularTotalGeral(),
                itens);
    }

    private PropostaPdfItemSnapshot toItemSnapshot(ItemChecklist item) {
        List<PropostaPdfProdutoSnapshot> produtos = item.getProdutosOrcados().stream()
                .map(this::toProdutoSnapshot)
                .toList();

        return new PropostaPdfItemSnapshot(
                item.getItem().getParteDoVeiculo().name(),
                item.getNomeItemSnapshot(),
                item.getMaoDeObra() != null ? item.getMaoDeObra() : BigDecimal.ZERO,
                item.calcularTotalProdutos(),
                produtos);
    }

    private PropostaPdfProdutoSnapshot toProdutoSnapshot(ChecklistProduto produto) {
        return new PropostaPdfProdutoSnapshot(
                produto.getNomeProdutoSnapshot(),
                produto.getQuantidade(),
                produto.getValorUnitario(),
                produto.getMarca(),
                produto.getValorTotal());
    }
}