package com.meclist.usecase.cliente;

import com.meclist.domain.Cliente;
import com.meclist.domain.TokenDefinicaoSenha;
import com.meclist.domain.enums.Situacao;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.interfaces.PasswordEncrypter;
import com.meclist.interfaces.TokenDefinicaoSenhaGateway;
import com.meclist.dto.cliente.DefinirSenhaRequest;
import com.meclist.exception.EmailNaoEncontradoException;
import com.meclist.exception.TokenNaoEncontradoException;
import com.meclist.validator.ValidatorUtils;
import org.springframework.stereotype.Service;

@Service
public class DefinirSenhaClienteUseCase {

    private final TokenDefinicaoSenhaGateway tokenGateway;
    private final ClienteGateway clienteGateway;
    private final PasswordEncrypter encrypter;

    public DefinirSenhaClienteUseCase(TokenDefinicaoSenhaGateway tokenGateway,
                                      ClienteGateway clienteGateway,
                                      PasswordEncrypter encrypter) {
        this.tokenGateway = tokenGateway;
        this.clienteGateway = clienteGateway;
        this.encrypter = encrypter;
    }

    public void executar(DefinirSenhaRequest request) {
        ValidatorUtils.validarSenha(request.senha());

        TokenDefinicaoSenha token = tokenGateway.buscarPorToken(request.token())
                .orElseThrow(() -> new TokenNaoEncontradoException("Token inválido ou não encontrado."));

        if (!token.isValido()) {
            throw new TokenNaoEncontradoException("Token expirado ou já utilizado.");
        }

        Cliente cliente = clienteGateway.buscarPorEmail(token.getEmail())
                .orElseThrow(() -> new EmailNaoEncontradoException("Cliente não encontrado."));

        String senhaHash = encrypter.hash(request.senha());
        cliente.atualizarSenhaComHash(senhaHash);
        cliente.setSituacao(Situacao.ATIVO);
        clienteGateway.atualizarCliente(cliente);

        token.marcarComoUsado();
        tokenGateway.salvar(token);
    }
}
