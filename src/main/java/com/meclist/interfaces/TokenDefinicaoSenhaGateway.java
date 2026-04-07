package com.meclist.interfaces;

import com.meclist.domain.TokenDefinicaoSenha;

import java.util.Optional;

public interface TokenDefinicaoSenhaGateway {
    TokenDefinicaoSenha salvar(TokenDefinicaoSenha token);
    Optional<TokenDefinicaoSenha> buscarPorToken(String token);
    Optional<TokenDefinicaoSenha> buscarUltimoPorEmail(String email);
}
