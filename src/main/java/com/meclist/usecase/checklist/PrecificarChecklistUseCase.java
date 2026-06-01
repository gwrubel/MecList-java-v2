package com.meclist.usecase.checklist;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Checklist;
import com.meclist.domain.ChecklistProduto;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.Orcamento;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.checklist.precificacao.PrecificarChecklistRequest;
import com.meclist.dto.checklist.precificacao.PrecificarItemRequest;
import com.meclist.dto.checklist.precificacao.PrecificarProdutoRequest;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.exception.ItemNaoPertenceAoChecklistException;
import com.meclist.domain.Produto;
import com.meclist.exception.ItemNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ChecklistProdutoGateway;
import com.meclist.interfaces.ItemChecklistGateway;
import com.meclist.interfaces.OrcamentoGateway;
import com.meclist.interfaces.ProdutoGateway;
import com.meclist.validator.ChecklistPrecificacaoValidator;


@Service
public class PrecificarChecklistUseCase {

    @PersistenceContext
    private EntityManager entityManager;

    private final ChecklistGateway checklistGateway;
    private final OrcamentoGateway orcamentoGateway;
    private final ItemChecklistGateway itemChecklistGateway;
    private final ChecklistWorkflowGuard workflowGuard;
    private final ChecklistPrecificacaoValidator precificacaoValidator;
    private final ChecklistProdutoGateway checklistProdutoGateway;
    private final ProdutoGateway produtoGateway;

    public PrecificarChecklistUseCase(
            ChecklistGateway checklistGateway,
            OrcamentoGateway orcamentoGateway,
            ItemChecklistGateway itemChecklistGateway,
            ChecklistWorkflowGuard workflowGuard,
            ChecklistPrecificacaoValidator precificacaoValidator,
            ChecklistProdutoGateway checklistProdutoGateway,
            ProdutoGateway produtoGateway) {
        this.checklistGateway = checklistGateway;
        this.orcamentoGateway = orcamentoGateway;
        this.itemChecklistGateway = itemChecklistGateway;
        this.workflowGuard = workflowGuard;
        this.precificacaoValidator = precificacaoValidator;
        this.checklistProdutoGateway = checklistProdutoGateway;
        this.produtoGateway = produtoGateway;
    }

    @Transactional
    public void executar(Long checklistId, PrecificarChecklistRequest request) {
        Checklist checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException("Checklist não encontrado: " + checklistId));

        workflowGuard.validarPrecificacaoPorAdm(checklist);

        // 1) garante que request cobre exatamente os itens TROCAR
        precificacaoValidator.validarCoberturaItensTrocar(checklist, request);

        Map<Long, ItemChecklist> itensNoBanco = checklist.getItensChecklist().stream()
                .collect(Collectors.toMap(ItemChecklist::getId, Function.identity()));

        // 2) aplica atualização item a item
        request.itens().forEach(itemReq -> {
            ItemChecklist item = itensNoBanco.get(itemReq.itemChecklistId());
            if (item == null) {
                throw new ItemNaoPertenceAoChecklistException(itemReq.itemChecklistId());
            }

            atualizarDadosDoItem(item, itemReq);
            itemChecklistGateway.salvar(item);
        });

        // 3) flush envia os deletes/inserts ao banco; clear descarta o cache de 1º nível
        //    para que o reload busque dados frescos e não as entidades em memória
        entityManager.flush();
        entityManager.clear();

        // 4) recarrega checklist com estado real do banco
        Checklist checklistAtualizado = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException("Checklist não encontrado: " + checklistId));

        // 4) valida se todos os itens TROCAR estão completamente precificados
        precificacaoValidator.validarCompletudeDosItensTrocar(checklistAtualizado);

        // 5) calcula total e persiste orçamento
        BigDecimal totalCalculado = checklistAtualizado.calcularTotalGeral();
        Orcamento orcamento = Orcamento.novo(checklistAtualizado, totalCalculado, StatusProcesso.AGUARDANDO_APROVACAO);
        orcamentoGateway.salvar(orcamento);

        // 6) finaliza checklist
        checklistAtualizado.finalizarPrecificacao();
        checklistGateway.salvar(checklistAtualizado);
    }

  private void atualizarDadosDoItem(ItemChecklist item, PrecificarItemRequest req) {
    item.definirMaoDeObra(req.maoDeObra());

    // IDs que o admin quer manter (produtos existentes enviados no request)
    Set<Long> idsNoRequest = req.produtos() == null ? Set.of() :
            req.produtos().stream()
                    .filter(p -> p.checklistProdutoId() != null)
                    .map(PrecificarProdutoRequest::checklistProdutoId)
                    .collect(Collectors.toSet());

    // Produtos que estão no banco mas não vieram no request → excluir
    List<Long> paraExcluir = item.getProdutosOrcados().stream()
            .map(p -> p.getId())
            .filter(id -> !idsNoRequest.contains(id))
            .toList();

    if (!paraExcluir.isEmpty()) {
        checklistProdutoGateway.deletarPorIds(paraExcluir);
    }

    if (req.produtos() != null) {
        req.produtos().forEach(prodReq -> {
            if (prodReq.checklistProdutoId() != null) {
                // Produto existente: atualiza precificação
                item.getProdutosOrcados().stream()
                        .filter(p -> p.getId().equals(prodReq.checklistProdutoId()))
                        .findFirst()
                        .ifPresent(p -> {
                            p.atualizarPrecificacao(prodReq.valorUnitario(), prodReq.marca());
                            p.setItemChecklist(item);
                            checklistProdutoGateway.salvar(p);
                        });
            } else {
                // Produto novo: cria e persiste
                if (prodReq.produtoId() == null || prodReq.quantidade() == null) {
                    throw new ItemNaoEncontradoException(
                            "Produto novo requer produtoId e quantidade");
                }
                Produto produto = produtoGateway.buscarPorId(prodReq.produtoId())
                        .orElseThrow(() -> new ItemNaoEncontradoException(
                                "Produto não encontrado: " + prodReq.produtoId()));
                ChecklistProduto novo = ChecklistProduto.novoComValorEMarca(
                        item, produto, prodReq.quantidade(),
                        prodReq.valorUnitario(), prodReq.marca());
                checklistProdutoGateway.salvar(novo);
            }
        });
    }
}
}