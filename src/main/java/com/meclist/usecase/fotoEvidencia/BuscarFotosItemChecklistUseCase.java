package com.meclist.usecase.fotoEvidencia;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meclist.dto.fotoEvidencia.FotoEvidenciaResponse;
import com.meclist.interfaces.FotoEvidenciaGateway;
import com.meclist.interfaces.StorageService;

@Service
public class BuscarFotosItemChecklistUseCase {
    private final FotoEvidenciaGateway fotoEvidenciaGateway;
    private final StorageService storageService;

    public BuscarFotosItemChecklistUseCase(FotoEvidenciaGateway fotoEvidenciaGateway, StorageService storageService) {
        this.fotoEvidenciaGateway = fotoEvidenciaGateway;
        this.storageService = storageService;
    }

    public List<FotoEvidenciaResponse> executar(Long idItemChecklist) {
       var fotos = fotoEvidenciaGateway.buscarPorItemChecklist(idItemChecklist);

        return fotos.stream()
                .map(foto -> {
                    String signedUrl = storageService
                            .generateSignedUrl(foto.getPathFoto(), 300); // 5 min

                    return new FotoEvidenciaResponse(
                            foto.getId(),
                            signedUrl,
                            foto.getCriadoEm()
                    );
                })
                .toList();
      
    }
}
