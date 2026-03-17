package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;

import com.meclist.domain.ItemChecklist;
import com.meclist.domain.enums.CategoriaParteVeiculo;

@Service
public class ItemChecklistCategoriaValidator {

    public void validar(ItemChecklist itemChecklist, Long checklistId, CategoriaParteVeiculo categoria) {
        if (!itemChecklist.getChecklist().getId().equals(checklistId)) {
            throw new IllegalArgumentException(
                    "ItemChecklist " + itemChecklist.getId() +
                            " não pertence ao Checklist " + checklistId);
        }

        if (!itemChecklist.getItem().getParteDoVeiculo().equals(categoria)) {
            throw new IllegalArgumentException(
                    "Item " + itemChecklist.getId() +
                            " não pertence à categoria " + categoria);
        }
    }
}
