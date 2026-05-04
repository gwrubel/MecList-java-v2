package com.meclist.usecase.cliente;

import com.meclist.domain.TokenDefinicaoSenha;
import com.meclist.domain.enums.Situacao;
import com.meclist.dto.cliente.RecuperarSenhaRequest;
import com.meclist.infra.EmailService;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.interfaces.TokenDefinicaoSenhaGateway;
import org.springframework.stereotype.Service;

@Service
public class SolicitarRecuperacaoSenhaClienteUseCase {

    private static final int HORAS_VALIDADE_TOKEN = 2;

    private final ClienteGateway clienteGateway;
    private final TokenDefinicaoSenhaGateway tokenGateway;
    private final EmailService emailService;

    public SolicitarRecuperacaoSenhaClienteUseCase(ClienteGateway clienteGateway,
                                                   TokenDefinicaoSenhaGateway tokenGateway,
                                                   EmailService emailService) {
        this.clienteGateway = clienteGateway;
        this.tokenGateway = tokenGateway;
        this.emailService = emailService;
    }

    public void executar(RecuperarSenhaRequest request) {
        var clienteOpt = clienteGateway.buscarPorEmail(request.email());
        if (clienteOpt.isEmpty()) {
            return;
        }

        var cliente = clienteOpt.get();
        if (cliente.getSituacao() != Situacao.ATIVO) {
            return;
        }

        tokenGateway.buscarUltimoPorEmail(cliente.getEmail())
                .filter(TokenDefinicaoSenha::isValido)
                .ifPresent(tokenAnterior -> {
                    tokenAnterior.marcarComoUsado();
                    tokenGateway.salvar(tokenAnterior);
                });

        TokenDefinicaoSenha token = TokenDefinicaoSenha.gerar(cliente.getEmail(), HORAS_VALIDADE_TOKEN);
        tokenGateway.salvar(token);

        emailService.enviarEmailRecuperacaoSenha(cliente.getEmail(), token.getToken());
    }
}
