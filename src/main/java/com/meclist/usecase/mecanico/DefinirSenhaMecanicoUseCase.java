package com.meclist.usecase.mecanico;

import com.meclist.domain.Mecanico;
import com.meclist.domain.TokenDefinicaoSenha;
import com.meclist.dto.mecanico.DefinirSenhaMecanicoRequest;
import com.meclist.exception.EmailNaoEncontradoException;
import com.meclist.exception.TokenNaoEncontradoException;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.interfaces.PasswordEncrypter;
import com.meclist.interfaces.TokenDefinicaoSenhaGateway;
import com.meclist.validator.ValidatorUtils;
import org.springframework.stereotype.Service;

@Service
public class DefinirSenhaMecanicoUseCase {

    private final TokenDefinicaoSenhaGateway tokenGateway;
    private final MecanicoGateway mecanicoGateway;
    private final PasswordEncrypter encrypter;

    public DefinirSenhaMecanicoUseCase(TokenDefinicaoSenhaGateway tokenGateway,
                                       MecanicoGateway mecanicoGateway,
                                       PasswordEncrypter encrypter) {
        this.tokenGateway = tokenGateway;
        this.mecanicoGateway = mecanicoGateway;
        this.encrypter = encrypter;
    }

    public void executar(DefinirSenhaMecanicoRequest request) {
        ValidatorUtils.validarSenha(request.senha());

        TokenDefinicaoSenha token = tokenGateway.buscarPorToken(request.token())
                .orElseThrow(() -> new TokenNaoEncontradoException("Token inválido ou não encontrado."));

        if (!token.isValido()) {
            throw new TokenNaoEncontradoException("Token expirado ou já utilizado.");
        }

        Mecanico mecanico = mecanicoGateway.buscarPorEmail(token.getEmail())
                .orElseThrow(() -> new EmailNaoEncontradoException("Mecânico não encontrado."));

        String senhaHash = encrypter.hash(request.senha());
        mecanico.atualizarSenhaComHash(senhaHash);
        mecanicoGateway.atualizarMecanico(mecanico);

        token.marcarComoUsado();
        tokenGateway.salvar(token);
    }
}
