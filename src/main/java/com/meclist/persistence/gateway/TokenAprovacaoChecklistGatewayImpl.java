package com.meclist.persistence.gateway;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.meclist.domain.TokenAprovacaoChecklist;
import com.meclist.interfaces.TokenAprovacaoChecklistGateway;
import com.meclist.persistence.entity.TokenAprovacaoChecklistEntity;
import com.meclist.persistence.repository.TokenAprovacaoChecklistRepository;

@Component
public class TokenAprovacaoChecklistGatewayImpl implements TokenAprovacaoChecklistGateway {

    private final TokenAprovacaoChecklistRepository repository;

    public TokenAprovacaoChecklistGatewayImpl(TokenAprovacaoChecklistRepository repository) {
        this.repository = repository;
    }

    @Override
    public TokenAprovacaoChecklist salvar(TokenAprovacaoChecklist token) {
        TokenAprovacaoChecklistEntity entity = toEntity(token);
        TokenAprovacaoChecklistEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<TokenAprovacaoChecklist> buscarPorToken(String token) {
        return repository.findByToken(token).map(this::toDomain);
    }

    private TokenAprovacaoChecklistEntity toEntity(TokenAprovacaoChecklist domain) {
        TokenAprovacaoChecklistEntity entity = new TokenAprovacaoChecklistEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setClienteId(domain.getClienteId());
        entity.setChecklistId(domain.getChecklistId());
        entity.setUsado(domain.isUsado());
        entity.setExpiraEm(domain.getExpiraEm());
        entity.setCriadoEm(domain.getCriadoEm());
        return entity;
    }

    private TokenAprovacaoChecklist toDomain(TokenAprovacaoChecklistEntity entity) {
        TokenAprovacaoChecklist domain = new TokenAprovacaoChecklist();
        domain.setId(entity.getId());
        domain.setToken(entity.getToken());
        domain.setClienteId(entity.getClienteId());
        domain.setChecklistId(entity.getChecklistId());
        domain.setUsado(entity.isUsado());
        domain.setExpiraEm(entity.getExpiraEm());
        domain.setCriadoEm(entity.getCriadoEm());
        return domain;
    }
}