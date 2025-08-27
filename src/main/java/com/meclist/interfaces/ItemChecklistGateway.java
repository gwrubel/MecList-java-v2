package com.meclist.interfaces;

import java.util.List;

import com.meclist.domain.ItemChecklist;

public interface ItemChecklistGateway {
    List<ItemChecklist> buscarPorChecklist(Long idChecklist);
}