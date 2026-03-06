package com.meclist.dto.checklist;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.itemChecklist.ItemChecklistResponse;



public record ChecklistResponse(
        Long checklistId,
        Long veiculoId,
        StatusProcesso status,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        Map<CategoriaParteVeiculo, List<ItemChecklistResponse>> itensPorCategoria) {

}
