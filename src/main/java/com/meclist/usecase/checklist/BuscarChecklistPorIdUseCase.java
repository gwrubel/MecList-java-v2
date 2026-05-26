package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;

import com.meclist.dto.checklist.ChecklistResponse;
import com.meclist.exception.AcessoNegadoException;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.mapper.ChecklistMapper;
import com.meclist.security.AuthenticatedUserProvider;

@Service
public class BuscarChecklistPorIdUseCase {

    private final ChecklistGateway checklistGateway;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public BuscarChecklistPorIdUseCase(ChecklistGateway checklistGateway,
                                       AuthenticatedUserProvider authenticatedUserProvider) {
        this.checklistGateway = checklistGateway;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    public ChecklistResponse executar(Long checklistId) {
        var checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() ->
                        new ChecklistNaoEncontradoException("Checklist não encontrado!"));

        var user = authenticatedUserProvider.get();

        if ("MECANICO".equals(user.role())) {
            Long mecanicoAtribuido = checklist.getMecanico() != null ? checklist.getMecanico().getId() : null;
            if (mecanicoAtribuido == null || !mecanicoAtribuido.equals(user.id())) {
                throw new AcessoNegadoException("Este checklist pertence a outro mecânico.");
            }
        }

        return ChecklistMapper.toResponse(checklist);
    }
}
