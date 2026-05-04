package com.meclist.usecase.fotoEvidencia;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.meclist.dto.fotoEvidencia.FotoEvidenciaResponse;
import com.meclist.interfaces.FotoEvidenciaGateway;
import com.meclist.interfaces.StorageService;

@Service
public class BuscarFotosItemChecklistUseCase {
    private static final Logger log = LoggerFactory.getLogger(BuscarFotosItemChecklistUseCase.class);

    private static final int SIGNED_URL_EXPIRATION_SECONDS = 1800;

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
                String signedUrl = gerarSignedUrlComFallback(foto.getPathFoto());

                    return new FotoEvidenciaResponse(
                            foto.getId(),
                            signedUrl,
                            foto.getCriadoEm()
                    );
                })
                .toList();
      
    }

    private String gerarSignedUrlComFallback(String path) {
        try {
            return storageService.generateSignedUrl(path, SIGNED_URL_EXPIRATION_SECONDS);
        } catch (RuntimeException ex) {
            log.warn("Falha ao gerar signed URL para '{}'. Retornando path original.", path, ex);
            return path;
        }
    }
}
