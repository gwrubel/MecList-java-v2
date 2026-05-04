package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.checklist.aprovacaoLink.ValidarLinkAprovacaoRequest;
import com.meclist.dto.checklist.aprovacaoLink.ValidarLinkAprovacaoResponse;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.exception.ClienteNaoEncontradoException;
import com.meclist.exception.LinkAprovacaoInvalidoException;
import com.meclist.exception.TokenNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.interfaces.TokenAprovacaoChecklistGateway;
import com.meclist.security.JwtService;

@Service
public class ValidarLinkAprovacaoChecklistUseCase {

    private final TokenAprovacaoChecklistGateway tokenAprovacaoChecklistGateway;
    private final ChecklistGateway checklistGateway;
    private final ClienteGateway clienteGateway;
    private final JwtService jwtService;

    public ValidarLinkAprovacaoChecklistUseCase(TokenAprovacaoChecklistGateway tokenAprovacaoChecklistGateway,
                                                ChecklistGateway checklistGateway,
                                                ClienteGateway clienteGateway,
                                                JwtService jwtService) {
        this.tokenAprovacaoChecklistGateway = tokenAprovacaoChecklistGateway;
        this.checklistGateway = checklistGateway;
        this.clienteGateway = clienteGateway;
        this.jwtService = jwtService;
    }

    @Transactional
    public ValidarLinkAprovacaoResponse executar(ValidarLinkAprovacaoRequest request) {
        var token = tokenAprovacaoChecklistGateway.buscarPorToken(request.token())
                .orElseThrow(() -> new TokenNaoEncontradoException("Token de aprovação não encontrado."));

        if (!token.getChecklistId().equals(request.checklistId())) {
            throw new LinkAprovacaoInvalidoException("Token não corresponde ao checklist informado.");
        }

        if (!token.isValido()) {
            throw new LinkAprovacaoInvalidoException("Link de aprovação inválido ou expirado.");
        }

        var checklist = checklistGateway.buscarPorId(request.checklistId())
                .orElseThrow(() -> new ChecklistNaoEncontradoException(
                        "Checklist não encontrado: " + request.checklistId()));

        if (checklist.getStatus() != StatusProcesso.AGUARDANDO_APROVACAO) {
            throw new LinkAprovacaoInvalidoException("Checklist não está disponível para aprovação.");
        }

        var cliente = clienteGateway.buscarPorId(token.getClienteId())
                .orElseThrow(() -> new ClienteNaoEncontradoException(
                        "Cliente não encontrado para o token de aprovação."));

        token.marcarComoUsado();
        tokenAprovacaoChecklistGateway.salvar(token);

        String jwt = jwtService.gerarTokenClienteAprovacaoChecklist(cliente, checklist.getId(), 60);
        return new ValidarLinkAprovacaoResponse(jwt, checklist.getId());
    }
}