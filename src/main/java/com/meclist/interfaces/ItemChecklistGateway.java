package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;

import com.meclist.domain.ItemChecklist;

public interface ItemChecklistGateway {
    ItemChecklist salvar(ItemChecklist itemChecklist);
    Optional<ItemChecklist> buscarPorId(Long id);
    List<ItemChecklist> buscarPorChecklist(Long checklistId);
    void salvarTodos(List<ItemChecklist> itensChecklist);
}
