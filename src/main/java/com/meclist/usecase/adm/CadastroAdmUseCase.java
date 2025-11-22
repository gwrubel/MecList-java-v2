package com.meclist.usecase.adm;

import com.meclist.domain.Adm;
import com.meclist.dto.adm.AdmRequest;
import com.meclist.interfaces.AdmGateway;
import com.meclist.interfaces.PasswordEncrypter;
import com.meclist.validator.ValidatorUtils;

import org.springframework.stereotype.Service;

@Service
public class CadastroAdmUseCase {
    private final AdmGateway gateway;
    private final PasswordEncrypter encrypter;

    public CadastroAdmUseCase(AdmGateway gateway, PasswordEncrypter encrypter) {
        this.gateway = gateway;
        this.encrypter = encrypter;
    }

   public void cadastrarAdm(AdmRequest request) {
    ValidatorUtils.validarEmail(request.email());
    ValidatorUtils.validarSenha(request.senha());

    if (gateway.buscarPorEmail(request.email()).isPresent()) {
        throw new IllegalArgumentException("Já existe um administrador com esse e-mail.");
    }

    String senhaHash = encrypter.hash(request.senha());

    Adm novoAdm = Adm.novoCadastro(request.nome(), request.email(), senhaHash);

    gateway.cadastrarAdm(novoAdm);
}

}
