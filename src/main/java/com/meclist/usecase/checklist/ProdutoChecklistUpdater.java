package com.meclist.usecase.checklist;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.meclist.domain.ChecklistProduto;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.Produto;
import com.meclist.dto.produto.ProdutoAdicionado;
import com.meclist.exception.ProdutoNaoEncontradoException;
import com.meclist.interfaces.ChecklistProdutoGateway;

@Service
public class ProdutoChecklistUpdater {

    private final ChecklistProdutoGateway checklistProdutoGateway;

    public ProdutoChecklistUpdater(ChecklistProdutoGateway checklistProdutoGateway) {
        this.checklistProdutoGateway = checklistProdutoGateway;
    }

    public void atualizarProdutos(
            ItemChecklist itemChecklist,
            List<ProdutoAdicionado> produtosAdicionados,
            Map<Long, Produto> produtosMap) {

        if (produtosAdicionados == null) {
            return;
        }

        List<ChecklistProduto> produtosAntigos = checklistProdutoGateway.buscarPorItemChecklist(itemChecklist.getId());

        if (!produtosAntigos.isEmpty()) {
            checklistProdutoGateway.deletarPorIds(
                    produtosAntigos.stream()
                            .map(ChecklistProduto::getId)
                            .collect(Collectors.toList()));
        }

        itemChecklist.limparProdutosOrcados();

        if (produtosAdicionados.isEmpty()) {
            return;
        }

        for (ProdutoAdicionado produtoRequest : produtosAdicionados) {
            Produto produto = produtosMap.get(produtoRequest.produtoId());
            if (produto == null) {
                throw new ProdutoNaoEncontradoException("Produto não encontrado: " + produtoRequest.produtoId());
            }

            ChecklistProduto checklistProduto = ChecklistProduto.novo(
                    itemChecklist,
                    produto,
                    produtoRequest.quantidade()
            );

            ChecklistProduto produtoSalvo = checklistProdutoGateway.salvar(checklistProduto);
            itemChecklist.adicionarProdutoOrcado(produtoSalvo);
        }
    }
}
