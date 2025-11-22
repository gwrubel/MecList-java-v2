package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;

import com.meclist.domain.ChecklistProduto;

public interface ChecklistProdutoGateway {
    ChecklistProduto salvar(ChecklistProduto checklistProduto);
    List<ChecklistProduto> buscarPorItemChecklist(Long idItemChecklist);
    List<ChecklistProduto> buscarPorItemChecklistEAprovacao(Long idItemChecklist, Boolean aprovado);
    List<ChecklistProduto> buscarPendentesPorItemChecklist(Long idItemChecklist);
    Optional<ChecklistProduto> buscarPorId(Long id);
    void excluir(Long id);
}

