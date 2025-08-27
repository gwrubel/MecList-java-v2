package com.meclist.usecase.adm;

import org.springframework.stereotype.Service;

import com.meclist.domain.Adm;
import com.meclist.interfaces.AdmGateway;
import com.meclist.interfaces.PasswordEncrypter;
import com.meclist.security.JwtService;


@Service
public class AutenticarAdmUseCase {
    private final AdmGateway gateway;
    private final JwtService jwtService;
    private final PasswordEncrypter encrypter;

    public AutenticarAdmUseCase(AdmGateway gateway, JwtService jwtService, PasswordEncrypter encrypter) {
        this.gateway = gateway;
        this.jwtService = jwtService;
        this.encrypter = encrypter;
    }

    public String autenticar(String email, String senha) {
        Adm adm = gateway.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("E-mail inválido!"));

        if (!encrypter.verificar(senha, adm.getSenha())) {
            throw new IllegalArgumentException("E-mail ou senha inválidos!");
        }

        return jwtService.gerarTokenAdm(adm); // tudo certo, retorna o token
    }
}
