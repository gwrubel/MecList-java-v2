package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;
import com.meclist.domain.ChecklistProduto;

public interface ChecklistProdutoGateway {
    ChecklistProduto salvar(ChecklistProduto checklistProduto);
    Optional<ChecklistProduto> buscarPorId(Long id);
    List<ChecklistProduto> buscarPorItemChecklist(Long idItemChecklist);
    void deletarPorIds(List<Long> ids); 
}