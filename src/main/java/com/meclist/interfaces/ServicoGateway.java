package com.meclist.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.meclist.domain.Servico;
import com.meclist.domain.enums.StatusProcesso;

public interface ServicoGateway {

    Servico salvar(Servico servico);

    Optional<Servico> buscarPorId(Long id);

    List<Servico> buscarPorChecklistId(Long checklistId);

    Optional<Servico> buscarExecutorPorChecklistId(Long checklistId, StatusProcesso status);

    List<Servico> buscarPorMecanicoEStatuses(Long mecanicoId, Collection<StatusProcesso> statuses);

    List<Servico> buscarConcluidosPorMecanico(Long mecanicoId);
}
