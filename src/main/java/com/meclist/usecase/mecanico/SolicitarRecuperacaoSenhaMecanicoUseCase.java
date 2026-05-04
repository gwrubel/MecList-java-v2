package com.meclist.usecase.mecanico;

import com.meclist.domain.TokenDefinicaoSenha;
import com.meclist.domain.enums.Situacao;
import com.meclist.dto.mecanico.RecuperarSenhaMecanicoRequest;
import com.meclist.infra.EmailService;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.interfaces.TokenDefinicaoSenhaGateway;
import org.springframework.stereotype.Service;

@Service
public class SolicitarRecuperacaoSenhaMecanicoUseCase {

    private static final int HORAS_VALIDADE_TOKEN = 2;

    private final MecanicoGateway mecanicoGateway;
    private final TokenDefinicaoSenhaGateway tokenGateway;
    private final EmailService emailService;

    public SolicitarRecuperacaoSenhaMecanicoUseCase(MecanicoGateway mecanicoGateway,
                                                    TokenDefinicaoSenhaGateway tokenGateway,
                                                    EmailService emailService) {
        this.mecanicoGateway = mecanicoGateway;
        this.tokenGateway = tokenGateway;
        this.emailService = emailService;
    }

    public void executar(RecuperarSenhaMecanicoRequest request) {
        var mecanicoOpt = mecanicoGateway.buscarPorEmail(request.email());
        if (mecanicoOpt.isEmpty()) {
            return;
        }

        var mecanico = mecanicoOpt.get();
        if (mecanico.getSituacao() != Situacao.ATIVO) {
            return;
        }

        tokenGateway.buscarUltimoPorEmail(mecanico.getEmail())
                .filter(TokenDefinicaoSenha::isValido)
                .ifPresent(tokenAnterior -> {
                    tokenAnterior.marcarComoUsado();
                    tokenGateway.salvar(tokenAnterior);
                });

        TokenDefinicaoSenha token = TokenDefinicaoSenha.gerar(mecanico.getEmail(), HORAS_VALIDADE_TOKEN);
        tokenGateway.salvar(token);

        emailService.enviarEmailRecuperacaoSenhaMecanico(mecanico.getEmail(), token.getToken());
    }
}
