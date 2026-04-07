package com.meclist.usecase.cliente;

import com.meclist.domain.Cliente;
import com.meclist.domain.TokenDefinicaoSenha;
import com.meclist.domain.enums.Situacao;
import com.meclist.infra.EmailService;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.interfaces.TokenDefinicaoSenhaGateway;
import com.meclist.dto.cliente.PrimeiroAcessoRequest;
import com.meclist.exception.CampoInvalidoException;
import com.meclist.exception.EmailNaoEncontradoException;


import org.springframework.stereotype.Service;

@Service
public class SolicitarPrimeiroAcessoUseCase {

    private final ClienteGateway clienteGateway;
    private final TokenDefinicaoSenhaGateway tokenGateway;
    private final EmailService emailService;

    public SolicitarPrimeiroAcessoUseCase(ClienteGateway clienteGateway,
                                          TokenDefinicaoSenhaGateway tokenGateway,
                                          EmailService emailService) {
        this.clienteGateway = clienteGateway;
        this.tokenGateway = tokenGateway;
        this.emailService = emailService;
    }

    public void executar(PrimeiroAcessoRequest request) {
        Cliente cliente = clienteGateway.buscarPorEmail(request.email())
                .orElseThrow(() -> new EmailNaoEncontradoException("E-mail não encontrado no sistema."));

        if (cliente.getSituacao() != Situacao.PENDENTE) {
            throw new CampoInvalidoException("email", "Senha ja definida.");
        }

        TokenDefinicaoSenha token = TokenDefinicaoSenha.gerar(cliente.getEmail(), 48);
        tokenGateway.salvar(token);
        emailService.enviarEmailDefinicaoSenha(cliente.getEmail(), token.getToken());
    }
}
