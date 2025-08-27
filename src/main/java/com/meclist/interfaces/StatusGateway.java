package com.meclist.interfaces;

import com.meclist.domain.Status;
import java.util.Optional;

public interface StatusGateway {
    Optional<Status> buscarPorId(Long id);
    Optional<Status> buscarPorDescricao(String descricao);
}
