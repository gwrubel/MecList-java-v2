package com.meclist.dto.checklist;

import java.util.List;

import com.meclist.dto.itemChecklist.AtualizacaoItemChecklist;

public record SalvarItensPorCategoriaRequest(
    List<AtualizacaoItemChecklist> itens
) {
    
}
