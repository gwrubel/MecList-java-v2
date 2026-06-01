package com.meclist.usecase.adm;

import org.springframework.stereotype.Service;

import com.meclist.domain.Adm;
import com.meclist.domain.TokenDefinicaoSenha;
import com.meclist.dto.adm.DefinirSenhaAdmRequest;
import com.meclist.exception.EmailNaoEncontradoException;
import com.meclist.exception.TokenNaoEncontradoException;
import com.meclist.interfaces.AdmGateway;
import com.meclist.interfaces.PasswordEncrypter;
import com.meclist.interfaces.TokenDefinicaoSenhaGateway;
import com.meclist.validator.ValidatorUtils;

@Service
public class DefinirSenhaAdmUseCase {

    private final TokenDefinicaoSenhaGateway tokenGateway;
    private final AdmGateway admGateway;
    private final PasswordEncrypter encrypter;

    public DefinirSenhaAdmUseCase(TokenDefinicaoSenhaGateway tokenGateway,
                                  AdmGateway admGateway,
                                  PasswordEncrypter encrypter) {
        this.tokenGateway = tokenGateway;
        this.admGateway = admGateway;
        this.encrypter = encrypter;
    }

    public void executar(DefinirSenhaAdmRequest request) {
        ValidatorUtils.validarSenha(request.senha());

        TokenDefinicaoSenha token = tokenGateway.buscarPorToken(request.token())
                .orElseThrow(() -> new TokenNaoEncontradoException("Token inválido ou não encontrado."));

        if (!token.isValido()) {
            throw new TokenNaoEncontradoException("Token expirado ou já utilizado.");
        }

        Adm adm = admGateway.buscarPorEmail(token.getEmail())
                .orElseThrow(() -> new EmailNaoEncontradoException("Administrador não encontrado."));

        String senhaHash = encrypter.hash(request.senha());
        adm.atualizarSenhaComHash(senhaHash);
        admGateway.atualizarAdm(adm);

        token.marcarComoUsado();
        tokenGateway.salvar(token);
    }
}