package com.meclist.usecase.checklist;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.meclist.domain.Checklist;
import com.meclist.domain.ChecklistProduto;
import com.meclist.domain.Orcamento;
import com.meclist.domain.enums.OrigemAprovacao;
import com.meclist.dto.checklist.aprovacao.AprovarChecklistRequest;
import com.meclist.dto.checklist.aprovacao.AprovarProdutoRequest;
import com.meclist.exception.ItemNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ChecklistProdutoGateway;
import com.meclist.interfaces.OrcamentoGateway;

@Service
public class ProcessarAprovacaoChecklistService {

    private final ChecklistGateway checklistGateway;
    private final ChecklistProdutoGateway checklistProdutoGateway;
    private final OrcamentoGateway orcamentoGateway;

    public ProcessarAprovacaoChecklistService(ChecklistGateway checklistGateway,
                                             ChecklistProdutoGateway checklistProdutoGateway,
                                             OrcamentoGateway orcamentoGateway) {
        this.checklistGateway = checklistGateway;
        this.checklistProdutoGateway = checklistProdutoGateway;
        this.orcamentoGateway = orcamentoGateway;
    }

    public void processar(Checklist checklist,
                          Orcamento orcamento,
                          AprovarChecklistRequest request,
                          OrigemAprovacao origemAprovacao,
                          Long atorId,
                          String atorRole) {
        Map<Long, ChecklistProduto> produtosMap = checklist.getItensChecklist().stream()
                .flatMap(item -> item.getProdutosOrcados().stream())
                .collect(Collectors.toMap(ChecklistProduto::getId, Function.identity()));

        Set<Long> idsRecebidos = request.produtos().stream()
                .map(AprovarProdutoRequest::checklistProdutoId)
                .collect(Collectors.toSet());

        for (AprovarProdutoRequest produtoReq : request.produtos()) {
            ChecklistProduto produto = produtosMap.get(produtoReq.checklistProdutoId());
            if (produto == null) {
                throw new ItemNaoEncontradoException(
                        "Produto do checklist não encontrado: " + produtoReq.checklistProdutoId());
            }

            if (Boolean.TRUE.equals(produtoReq.aprovadoCliente())) {
                produto.aprovar();
            } else {
                produto.rejeitar();
            }

            produto.registrarDecisao(origemAprovacao, atorId, atorRole);
            checklistProdutoGateway.salvar(produto);
        }

        for (Map.Entry<Long, ChecklistProduto> entry : produtosMap.entrySet()) {
            if (!idsRecebidos.contains(entry.getKey())) {
                ChecklistProduto produto = entry.getValue();
                produto.rejeitar();
                produto.registrarDecisao(origemAprovacao, atorId, atorRole);
                checklistProdutoGateway.salvar(produto);
            }
        }

        boolean algumAprovado = produtosMap.values().stream()
                .anyMatch(ChecklistProduto::estaAprovado);

        checklist.registrarAprovacao(origemAprovacao, atorId, atorRole);

        if (algumAprovado) {
            orcamento.aprovar();
            checklist.aprovar();
        } else {
            orcamento.rejeitar();
            checklist.reprovar();
        }

        orcamento.setOrigemAprovacaoFinal(origemAprovacao);
        if (origemAprovacao == OrigemAprovacao.ADMIN_MANUAL) {
            orcamento.finalizarFluxoManual(origemAprovacao);
        }

        orcamentoGateway.salvar(orcamento);
        checklistGateway.salvar(checklist);
    }
}