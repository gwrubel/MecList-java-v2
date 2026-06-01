package com.meclist.usecase.adm;

import org.springframework.stereotype.Service;

import com.meclist.domain.TokenDefinicaoSenha;
import com.meclist.domain.enums.Situacao;
import com.meclist.dto.adm.RecuperarSenhaAdmRequest;
import com.meclist.infra.EmailService;
import com.meclist.interfaces.AdmGateway;
import com.meclist.interfaces.TokenDefinicaoSenhaGateway;

@Service
public class SolicitarRecuperacaoSenhaAdmUseCase {

    private static final int HORAS_VALIDADE_TOKEN = 2;

    private final AdmGateway admGateway;
    private final TokenDefinicaoSenhaGateway tokenGateway;
    private final EmailService emailService;

    public SolicitarRecuperacaoSenhaAdmUseCase(AdmGateway admGateway,
                                               TokenDefinicaoSenhaGateway tokenGateway,
                                               EmailService emailService) {
        this.admGateway = admGateway;
        this.tokenGateway = tokenGateway;
        this.emailService = emailService;
    }

    public void executar(RecuperarSenhaAdmRequest request) {
        var admOpt = admGateway.buscarPorEmail(request.email());
        if (admOpt.isEmpty()) {
            return;
        }

        var adm = admOpt.get();
        if (adm.getSituacao() != Situacao.ATIVO) {
            return;
        }

        tokenGateway.buscarUltimoPorEmail(adm.getEmail())
                .filter(TokenDefinicaoSenha::isValido)
                .ifPresent(tokenAnterior -> {
                    tokenAnterior.marcarComoUsado();
                    tokenGateway.salvar(tokenAnterior);
                });

        TokenDefinicaoSenha token = TokenDefinicaoSenha.gerar(adm.getEmail(), HORAS_VALIDADE_TOKEN);
        tokenGateway.salvar(token);

        emailService.enviarEmailRecuperacaoSenhaAdm(adm.getEmail(), token.getToken());
    }
}