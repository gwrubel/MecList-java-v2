package com.meclist.usecase.checklist;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Checklist;
import com.meclist.domain.ChecklistProduto;
import com.meclist.domain.Orcamento;
import com.meclist.dto.checklist.aprovacao.AprovarChecklistRequest;
import com.meclist.dto.checklist.aprovacao.AprovarProdutoRequest;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.exception.ItemNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ChecklistProdutoGateway;
import com.meclist.interfaces.OrcamentoGateway;

@Service
public class AprovarChecklistUseCase {

    private final ChecklistGateway checklistGateway;
    private final ChecklistProdutoGateway checklistProdutoGateway;
    private final OrcamentoGateway orcamentoGateway;
    private final ChecklistWorkflowGuard workflowGuard;

    public AprovarChecklistUseCase(ChecklistGateway checklistGateway,
                                   ChecklistProdutoGateway checklistProdutoGateway,
                                   OrcamentoGateway orcamentoGateway,
                                   ChecklistWorkflowGuard workflowGuard) {
        this.checklistGateway = checklistGateway;
        this.checklistProdutoGateway = checklistProdutoGateway;
        this.orcamentoGateway = orcamentoGateway;
        this.workflowGuard = workflowGuard;
    }

    @Transactional
    public void executar(Long checklistId, AprovarChecklistRequest request) {
        Checklist checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException(
                        "Checklist não encontrado: " + checklistId));

        workflowGuard.validarAprovacaoPorCliente(checklist);

        // Coleta todos os ChecklistProduto dos itens TROCAR em um mapa por ID
        Map<Long, ChecklistProduto> produtosMap = checklist.getItensChecklist().stream()
                .flatMap(item -> item.getProdutosOrcados().stream())
                .collect(Collectors.toMap(ChecklistProduto::getId, Function.identity()));

        // IDs que vieram no request (podem ser parciais ou vazios)
        Set<Long> idsRecebidos = request.produtos().stream()
                .map(AprovarProdutoRequest::checklistProdutoId)
                .collect(Collectors.toSet());

        // 1) Aplica decisão explícita do front nos produtos que vieram no request
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

            checklistProdutoGateway.salvar(produto);
        }

        // 2) Produtos que NÃO vieram no request são tratados como rejeitados
        for (Map.Entry<Long, ChecklistProduto> entry : produtosMap.entrySet()) {
            if (!idsRecebidos.contains(entry.getKey())) {
                ChecklistProduto produto = entry.getValue();
                produto.rejeitar();
                checklistProdutoGateway.salvar(produto);
            }
        }

        // 3) Decide status final: ao menos 1 aprovado = APROVADO, senão REPROVADO
        boolean algumAprovado = produtosMap.values().stream()
                .anyMatch(ChecklistProduto::estaAprovado);

        Orcamento orcamento = orcamentoGateway.buscarPorChecklistId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException(
                        "Orçamento não encontrado para o checklist: " + checklistId));

        if (algumAprovado) {
            orcamento.aprovar();
            checklist.aprovar();
        } else {
            orcamento.rejeitar();
            checklist.reprovar();
        }

        orcamentoGateway.salvar(orcamento);
        checklistGateway.salvar(checklist);
    }
}
