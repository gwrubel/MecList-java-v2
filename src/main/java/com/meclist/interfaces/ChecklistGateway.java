package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;

import com.meclist.domain.Checklist;

public interface ChecklistGateway {
    Checklist salvar(Checklist checklist);
    Optional<Checklist> buscarPorId(Long id);
    List<Checklist> buscarPorMecanico(Long mecanicoId);
}
