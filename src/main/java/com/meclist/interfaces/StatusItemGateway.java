package com.meclist.interfaces;

import com.meclist.domain.StatusItem;
import java.util.Optional;

public interface StatusItemGateway {
    Optional<StatusItem> buscarPorDescricao(String descricao);
    Optional<StatusItem> buscarPorId(Long id);
}