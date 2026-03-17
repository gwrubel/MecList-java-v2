package com.meclist.usecase.checklist;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.meclist.domain.Checklist;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.exception.AcessoNegadoException;
import com.meclist.exception.ChecklistStatusInvalidoException;
import com.meclist.security.AuthenticatedUser;

@Component
public class ChecklistWorkflowGuard {

    public void validarEdicaoItensPorMecanico(Checklist checklist) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new AcessoNegadoException("Usuário não autenticado.");
        }

        if (!"MECANICO".equals(user.role())) {
            throw new AcessoNegadoException("Apenas mecânico pode atualizar checklist.");
        }

        if (checklist.getStatus() != StatusProcesso.EM_ANDAMENTO) {
            throw new ChecklistStatusInvalidoException("Checklist já foi finalizado ou está em um status inválido.");
        }

        Long idMecanicoChecklist = checklist.getMecanico() != null ? checklist.getMecanico().getId() : null;
        if (idMecanicoChecklist == null || !idMecanicoChecklist.equals(user.id())) {
            throw new AcessoNegadoException("Este checklist pertence a outro mecânico.");
        }
    }
}