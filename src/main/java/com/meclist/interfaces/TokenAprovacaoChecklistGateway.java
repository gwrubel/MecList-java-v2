package com.meclist.interfaces;

import java.util.Optional;

import com.meclist.domain.TokenAprovacaoChecklist;

public interface TokenAprovacaoChecklistGateway {
    TokenAprovacaoChecklist salvar(TokenAprovacaoChecklist token);

    Optional<TokenAprovacaoChecklist> buscarPorToken(String token);
}