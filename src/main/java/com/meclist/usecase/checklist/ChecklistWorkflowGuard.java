package com.meclist.usecase.checklist;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.meclist.domain.Checklist;
import com.meclist.domain.Orcamento;
import com.meclist.domain.enums.EtapaFluxoManual;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.exception.AcessoNegadoException;
import com.meclist.exception.ChecklistStatusInvalidoException;
import com.meclist.security.AuthenticatedUser;
import com.meclist.security.AuthenticatedUserProvider;

@Component
public class ChecklistWorkflowGuard {

    private final AuthenticatedUserProvider authenticatedUserProvider;

    public ChecklistWorkflowGuard(AuthenticatedUserProvider authenticatedUserProvider) {
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

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
     * Cliente visualiza/aprova checklist já precificado ({@link StatusProcesso#AGUARDANDO_APROVACAO}).
     * Valida que o usuário autenticado é CLIENTE, que o checklist pertence a ele e está no status correto.
     */
    public void validarAprovacaoPorCliente(Checklist checklist) {
        AuthenticatedUser user = obterUsuarioAutenticado();

        if (!"CLIENTE".equals(user.role())) {
            throw new AcessoNegadoException("Apenas clientes podem aprovar o checklist.");
        }

        if (checklist.getStatus() != StatusProcesso.AGUARDANDO_APROVACAO) {
            throw new ChecklistStatusInvalidoException(
                    "O checklist não está aguardando aprovação. Status atual: " + checklist.getStatus());
        }

        Long idClienteDoVeiculo = checklist.getVeiculo() != null
                && checklist.getVeiculo().getCliente() != null
                        ? checklist.getVeiculo().getCliente().getId()
                        : null;

        if (idClienteDoVeiculo == null || !idClienteDoVeiculo.equals(user.id())) {
            throw new AcessoNegadoException("Este checklist não pertence ao cliente autenticado.");
        }

        validarEscopoChecklistSeTokenAprovacao(checklist);
    }

    /**
     * Administrador precifica checklist já enviado pelo mecânico ({@link StatusProcesso#AGUARDANDO_PRECIFICACAO}).
     */
    public void validarPrecificacaoPorAdm(Checklist checklist) {
        validarAdministrador(checklist, StatusProcesso.AGUARDANDO_PRECIFICACAO,
                "Apenas administradores podem realizar a precificação.",
                "O status atual (" + checklist.getStatus() + ") não permite precificação.");
    }

    public void validarInicioFluxoManualPorAdm(Checklist checklist) {
        validarAdministrador(checklist, StatusProcesso.AGUARDANDO_APROVACAO,
                "Apenas administradores podem iniciar o fluxo manual.",
                "O checklist não está aguardando aprovação para iniciar o fluxo manual.");
    }

    public void validarGeracaoPdfManualPorAdm(Checklist checklist, Orcamento orcamento) {
        validarInicioFluxoManualPorAdm(checklist);

        if (orcamento.getEtapaFluxoManual() == null || orcamento.getEtapaFluxoManual() == EtapaFluxoManual.NAO_INICIADO) {
            throw new ChecklistStatusInvalidoException(
                    "O fluxo manual precisa ser iniciado antes da geração do PDF.");
        }
    }

    public void validarRegistroConfirmacaoManualPorAdm(Checklist checklist, Orcamento orcamento) {
        validarInicioFluxoManualPorAdm(checklist);

        if (orcamento.getEtapaFluxoManual() == null || orcamento.getEtapaFluxoManual() == EtapaFluxoManual.NAO_INICIADO) {
            throw new ChecklistStatusInvalidoException(
                    "O fluxo manual precisa ser iniciado antes do registro da confirmação.");
        }
    }

    public void validarAprovacaoManualPorAdm(Checklist checklist, Orcamento orcamento) {
        validarInicioFluxoManualPorAdm(checklist);

        if (orcamento.getConfirmacaoManualEm() == null) {
            throw new ChecklistStatusInvalidoException(
                    "A confirmação manual do cliente deve ser registrada antes da aprovação administrativa.");
        }
    }

    public AuthenticatedUser obterUsuarioAutenticado() {
        return authenticatedUserProvider.get();
    }

    private void validarAdministrador(Checklist checklist,
                                      StatusProcesso statusEsperado,
                                      String mensagemAcessoNegado,
                                      String mensagemStatusInvalido) {
        AuthenticatedUser user = obterUsuarioAutenticado();

        if (!"ADMIN".equals(user.role()) && !"ADM".equals(user.role())) {
            throw new AcessoNegadoException(mensagemAcessoNegado);
        }

        if (checklist.getStatus() != statusEsperado) {
            throw new ChecklistStatusInvalidoException(mensagemStatusInvalido);
        }
    }

    private void validarOperacaoMecanicoDonoEmAndamento(Checklist checklist, String mensagemSomenteMecanico) {
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || !(auth.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new AcessoNegadoException("Usuário não autenticado.");
        }

        if (!"MECANICO".equals(user.role())) {
            throw new AcessoNegadoException(mensagemSomenteMecanico);
        }

        if (checklist.getStatus() != StatusProcesso.INICIADO && checklist.getStatus() != StatusProcesso.EM_ANDAMENTO){
            throw new ChecklistStatusInvalidoException(
                    "Checklist já foi finalizado ou está em um status inválido.");
        }

        Long idMecanicoChecklist = checklist.getMecanico() != null ? checklist.getMecanico().getId() : null;
        if (idMecanicoChecklist == null || !idMecanicoChecklist.equals(user.id())) {
            throw new AcessoNegadoException("Este checklist pertence a outro mecânico.");
        }
    }

    @SuppressWarnings("unchecked")
    private void validarEscopoChecklistSeTokenAprovacao(Checklist checklist) {
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getDetails() instanceof java.util.Map<?, ?> raw)) {
            return;
        }

        Map<String, Object> details = (Map<String, Object>) raw;
        Boolean approvalOnly = (Boolean) details.get("approvalOnly");
        if (!Boolean.TRUE.equals(approvalOnly)) {
            return;
        }

        Object checklistIdObj = details.get("checklistId");
        Long checklistId = checklistIdObj instanceof Number n ? n.longValue() : null;
        if (checklistId == null || !checklistId.equals(checklist.getId())) {
            throw new AcessoNegadoException("Token de aprovação não permite acesso a este checklist.");
        }
    }

}