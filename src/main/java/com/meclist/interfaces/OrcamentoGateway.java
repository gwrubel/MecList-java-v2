package com.meclist.interfaces;

import com.meclist.domain.Orcamento;
import java.util.Optional;

public interface OrcamentoGateway {
    Orcamento salvar(Orcamento orcamento);
    Optional<Orcamento> buscarPorChecklistId(Long checklistId);
}
