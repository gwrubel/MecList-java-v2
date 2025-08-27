package com.meclist.usecase.mecanico;

import org.springframework.stereotype.Service;

import com.meclist.domain.enums.Situacao;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.interfaces.PasswordEncrypter;
import com.meclist.security.JwtService;

@Service
public class AuthMecanicoUseCase {
    private final MecanicoGateway mecanicoGateway;
    private final JwtService jwtService;
    private final PasswordEncrypter encrypter;

    public AuthMecanicoUseCase(MecanicoGateway mecanicoGateway, JwtService jwtService, PasswordEncrypter encrypter) {
        this.mecanicoGateway = mecanicoGateway;
        this.jwtService = jwtService;
        this.encrypter = encrypter;
    }

    
    public String autenticar(String email, String senha) {
        var mecanico = mecanicoGateway.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("E-mail inválido!"));

        if (!encrypter.verificar(senha, mecanico.getSenha())) {
            throw new IllegalArgumentException("E-mail ou senha inválidos!");
        }

        if (mecanico.getSituacao() == Situacao.INATIVO) {
            throw new IllegalArgumentException("Cadastro inativo!");
        }
        
       
        return jwtService.gerarTokenMecanico(mecanico); // tudo certo, retorna o token
    }
}
