package com.meclist.usecase.cliente;

import org.springframework.stereotype.Service;

import com.meclist.interfaces.ClienteGateway;
import com.meclist.interfaces.PasswordEncrypter;
import com.meclist.security.JwtService;

@Service
public class AutenticarClienteUseCase {
    private final ClienteGateway clienteGateway;
    private final PasswordEncrypter encrypter;
    private final JwtService jwtService;

    public AutenticarClienteUseCase(ClienteGateway clienteGateway, PasswordEncrypter encrypter, JwtService jwtService) {
        this.clienteGateway = clienteGateway;
        this.encrypter = encrypter;
        this.jwtService = jwtService;
    }

    public String autenticar(String email, String senha) {
        var cliente = clienteGateway.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("E-mail inválido!"));
        if (!encrypter.verificar(senha, cliente.getSenha())) {
            throw new IllegalArgumentException("E-mail ou senha inválidos!");
        }
        return jwtService.gerarTokenCliente(cliente); // tudo certo, retorna o token
    }

}
