package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;

import com.meclist.domain.Checklist;
import com.meclist.domain.enums.StatusProcesso;

public interface ChecklistGateway {
    Checklist salvar(Checklist checklist);
    Optional<Checklist> buscarPorId(Long id);
    List<Checklist> buscarPorMecanico(Long mecanicoId);
    void atualizarStatus(Checklist checklist);
    List<Checklist> buscarPorStatus (StatusProcesso status);
    List<Checklist> buscarDashboardPorCliente(Long clienteId);
}
