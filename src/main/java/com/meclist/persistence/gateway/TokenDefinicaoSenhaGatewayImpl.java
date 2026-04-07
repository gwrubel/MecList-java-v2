package com.meclist.persistence.gateway;

import com.meclist.domain.TokenDefinicaoSenha;
import com.meclist.interfaces.TokenDefinicaoSenhaGateway;
import com.meclist.persistence.entity.TokenDefinicaoSenhaEntity;
import com.meclist.persistence.repository.TokenDefinicaoSenhaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenDefinicaoSenhaGatewayImpl implements TokenDefinicaoSenhaGateway {

    private final TokenDefinicaoSenhaRepository repository;

    public TokenDefinicaoSenhaGatewayImpl(TokenDefinicaoSenhaRepository repository) {
        this.repository = repository;
    }

    @Override
    public TokenDefinicaoSenha salvar(TokenDefinicaoSenha token) {
        TokenDefinicaoSenhaEntity entity = toEntity(token);
        TokenDefinicaoSenhaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<TokenDefinicaoSenha> buscarPorToken(String token) {
        return repository.findByToken(token).map(this::toDomain);
    }

    @Override
    public Optional<TokenDefinicaoSenha> buscarUltimoPorEmail(String email) {
        return repository.findFirstByEmailAndUsadoFalseOrderByCriadoEmDesc(email).map(this::toDomain);
    }

    private TokenDefinicaoSenhaEntity toEntity(TokenDefinicaoSenha domain) {
        TokenDefinicaoSenhaEntity entity = new TokenDefinicaoSenhaEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setEmail(domain.getEmail());
        entity.setUsado(domain.isUsado());
        entity.setExpiraEm(domain.getExpiraEm());
        entity.setCriadoEm(domain.getCriadoEm());
        return entity;
    }

    private TokenDefinicaoSenha toDomain(TokenDefinicaoSenhaEntity entity) {
        TokenDefinicaoSenha domain = new TokenDefinicaoSenha();
        domain.setId(entity.getId());
        domain.setToken(entity.getToken());
        domain.setEmail(entity.getEmail());
        domain.setUsado(entity.isUsado());
        domain.setExpiraEm(entity.getExpiraEm());
        domain.setCriadoEm(entity.getCriadoEm());
        return domain;
    }
}
