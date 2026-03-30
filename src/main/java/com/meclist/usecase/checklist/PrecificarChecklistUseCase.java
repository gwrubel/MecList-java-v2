package com.meclist.usecase.checklist;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Checklist;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.Orcamento;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.checklist.precificacao.PrecificarChecklistRequest;
import com.meclist.dto.checklist.precificacao.PrecificarItemRequest;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ItemChecklistGateway;
import com.meclist.interfaces.OrcamentoGateway;
import com.meclist.mapper.ChecklistMapper;

@Service
public class PrecificarChecklistUseCase {

    private final ChecklistGateway checklistGateway;
    private final ItemChecklistGateway itemChecklistGateway;
    private final OrcamentoGateway orcamentoGateway;

    public PrecificarChecklistUseCase(
        ChecklistGateway checklistGateway,
        ItemChecklistGateway itemChecklistGateway,
        OrcamentoGateway orcamentoGateway
    ) {
        this.checklistGateway = checklistGateway;
        this.itemChecklistGateway = itemChecklistGateway;
        this.orcamentoGateway = orcamentoGateway;
    }

    @Transactional
    public void executar(Long checklistId, PrecificarChecklistRequest request) {
        Checklist checklist = checklistGateway.buscarPorId(checklistId)
            .orElseThrow(() -> new IllegalArgumentException("Checklist não encontrado"));

        List<ItemChecklist> itensChecklist = itemChecklistGateway.buscarPorChecklist(checklistId);

        BigDecimal total = BigDecimal.ZERO;

        for (PrecificarItemRequest itemReq : request.itens()) {
            ItemChecklist item = itensChecklist.stream()
                .filter(i -> i.getId().equals(itemReq.itemChecklistId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ItemChecklist não encontrado: " + itemReq.itemChecklistId()));

            // Atualiza mão de obra
            item.definirMaoDeObra(itemReq.maoDeObra());
            total = total.add(itemReq.maoDeObra() != null ? itemReq.maoDeObra() : BigDecimal.ZERO);

            // Atualiza produtos
            if (itemReq.produtos() != null) {
                itemReq.produtos().forEach(prodReq -> {
                    item.getProdutosOrcados().stream()
                        .filter(p -> p.getId().equals(prodReq.checklistProdutoId()))
                        .findFirst()
                        .ifPresent(produto -> {
                            produto.setValorUnitario(prodReq.valorUnitario());
                            produto.setMarca(prodReq.marca());
                            // Soma ao total
                            if (prodReq.valorUnitario() != null && produto.getQuantidade() != null) {
                                total = total.add(prodReq.valorUnitario().multiply(new BigDecimal(produto.getQuantidade())));
                            }
                        });
                });
            }
        }

        // Persiste alterações nos itens
        itemChecklistGateway.salvarTodos(itensChecklist);

        // Cria e salva o orçamento
        Orcamento orcamento = Orcamento.novo(checklist, total, StatusProcesso.AGUARDANDO_APROVACAO);
        orcamentoGateway.salvar(orcamento);

        // Atualiza status do checklist
        checklist.setStatus(StatusProcesso.AGUARDANDO_APROVACAO);
        checklistGateway.salvar(checklist);

        // Retorna o response atualizado
        Checklist checklistAtualizado = checklistGateway.buscarPorId(checklistId)
            .orElseThrow(() -> new IllegalArgumentException("Checklist não encontrado após atualização"));

        
    }
}
