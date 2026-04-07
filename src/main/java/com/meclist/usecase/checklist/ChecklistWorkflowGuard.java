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
        validarOperacaoMecanicoDonoEmAndamento(
                checklist,
                "Apenas mecânico pode atualizar checklist.");
    }

    public void validarEnvioParaPrecificacaoPorMecanico(Checklist checklist) {
        validarOperacaoMecanicoDonoEmAndamento(
                checklist,
                "Apenas mecânico pode enviar o checklist para precificação.");
    }

    /**
     * Administrador precifica checklist já enviado pelo mecânico ({@link StatusProcesso#AGUARDANDO_PRECIFICACAO}).
     */
    public void validarPrecificacaoPorAdm(Checklist checklist) {
        AuthenticatedUser user = obterUsuarioAutenticado();

        if (!"ADMIN".equals(user.role()) && !"ADM".equals(user.role())) {
            throw new AcessoNegadoException("Apenas administradores podem realizar a precificação.");
        }

        if (checklist.getStatus() != StatusProcesso.AGUARDANDO_PRECIFICACAO) {
            throw new ChecklistStatusInvalidoException(
                    "O status atual (" + checklist.getStatus() + ") não permite precificação.");
        }
    }

    private void validarOperacaoMecanicoDonoEmAndamento(Checklist checklist, String mensagemSomenteMecanico) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || !(auth.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new AcessoNegadoException("Usuário não autenticado.");
        }

        if (!"MECANICO".equals(user.role())) {
            throw new AcessoNegadoException(mensagemSomenteMecanico);
        }

        if (checklist.getStatus() != StatusProcesso.EM_ANDAMENTO) {
            throw new ChecklistStatusInvalidoException(
                    "Checklist já foi finalizado ou está em um status inválido.");
        }

        Long idMecanicoChecklist = checklist.getMecanico() != null ? checklist.getMecanico().getId() : null;
        if (idMecanicoChecklist == null || !idMecanicoChecklist.equals(user.id())) {
            throw new AcessoNegadoException("Este checklist pertence a outro mecânico.");
        }
    }

    private AuthenticatedUser obterUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || !(auth.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new AcessoNegadoException("Usuário não autenticado.");
        }

        return user;
    }
}