package com.meclist.usecase.checklist;

import com.meclist.domain.Checklist;
import com.meclist.domain.Orcamento;

public record ChecklistOrcamentoContext(
        Checklist checklist,
        Orcamento orcamento
) {
}