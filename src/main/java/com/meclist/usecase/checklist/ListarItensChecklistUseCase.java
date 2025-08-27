package com.meclist.usecase.checklist;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.meclist.domain.ItemChecklist;
import com.meclist.dto.checklist.ItemChecklistResponse;
import com.meclist.interfaces.ItemChecklistGateway;
import com.meclist.interfaces.ItemGateway;
import com.meclist.interfaces.StatusItemGateway;

@Service
public class ListarItensChecklistUseCase {

    private final ItemChecklistGateway itemChecklistGateway;
    private final ItemGateway itemGateway;
    private final StatusItemGateway statusItemGateway;

    public ListarItensChecklistUseCase(ItemChecklistGateway itemChecklistGateway, 
                                      ItemGateway itemGateway,
                                      StatusItemGateway statusItemGateway) {
        this.itemChecklistGateway = itemChecklistGateway;
        this.itemGateway = itemGateway;
        this.statusItemGateway = statusItemGateway;
    }

    public List<ItemChecklistResponse> executar(Long idChecklist) {
        List<ItemChecklist> itensChecklist = itemChecklistGateway.buscarPorChecklist(idChecklist);
        
        return itensChecklist.stream()
                .map(itemChecklist -> {
                    var item = itemGateway.buscarPorId(itemChecklist.getIdItem())
                            .orElseThrow(() -> new RuntimeException("Item não encontrado"));
                    
                    var statusItem = statusItemGateway.buscarPorId(itemChecklist.getIdStatusItem())
                            .orElseThrow(() -> new RuntimeException("Status não encontrado"));
                    
                    return new ItemChecklistResponse(
                            itemChecklist.getId(),
                            itemChecklist.getIdChecklist(),
                            item.getId(),
                            item.getNome(),
                            item.getImagemIlustrativa(),
                            statusItem.getId(),
                            statusItem.getDescricao()
                    );
                })
                .collect(Collectors.toList());
    }
}