package com.meclist.usecase.cliente;

import org.springframework.stereotype.Service;

import com.meclist.dto.cliente.ChecklistCardResumo;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;


@Service
public class BuscarDetalhesChecklistUseCase {
    

    private final ChecklistGateway checklistGateway;

    public BuscarDetalhesChecklistUseCase(ChecklistGateway checklistGateway) {
        this.checklistGateway = checklistGateway;
    }

    
    public ChecklistCardResumo executar(Long checklistId) {
        var checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() ->
                        new ChecklistNaoEncontradoException("Checklist não encontrado!"));

       var checklistCardResumo = new ChecklistCardResumo(
                        checklist.getId(),
                        checklist.getVeiculo().getPlaca(),
                        checklist.getVeiculo().getMarca(),
                        checklist.getVeiculo().getModelo(),
                        checklist.getVeiculo().getAno(),
                        checklist.getDescricao(),
                        checklist.getMecanico().getNome(),
                        checklist.getVeiculo().getQuilometragem(),
                        checklist.getStatus()
                );
        return checklistCardResumo;
    }
}
