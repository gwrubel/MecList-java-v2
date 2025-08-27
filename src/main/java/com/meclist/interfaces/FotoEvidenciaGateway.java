package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;

import com.meclist.domain.FotoEvidencia;

public interface FotoEvidenciaGateway {
    FotoEvidencia salvar(FotoEvidencia foto);
    Optional<FotoEvidencia> buscarPorId(Long id);
    List<FotoEvidencia> buscarPorItemChecklist(Long idItemChecklist);
    void deletar(Long id);
}
