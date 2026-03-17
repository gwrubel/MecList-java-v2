package com.meclist.usecase.checklist;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.meclist.domain.FotoEvidencia;
import com.meclist.domain.ItemChecklist;
import com.meclist.dto.fotoEvidencia.FotoEvidenciaRequest;
import com.meclist.interfaces.FotoEvidenciaGateway;
import com.meclist.interfaces.StorageService;

@Service
public class FotoEvidenciaUpdater {

    private final FotoEvidenciaGateway fotoEvidenciaGateway;
    private final StorageService storageService;

    public FotoEvidenciaUpdater(FotoEvidenciaGateway fotoEvidenciaGateway, StorageService storageService) {
        this.fotoEvidenciaGateway = fotoEvidenciaGateway;
        this.storageService = storageService;
    }

    public void processarFotos(
            Long checklistId,
            ItemChecklist itemChecklist,
            List<FotoEvidenciaRequest> fotosRequest,
            Map<String, MultipartFile> arquivosPorChave,
            StorageCompensationContext contexto) throws IOException {

        if (fotosRequest == null) {
            return;
        }

        validarContratoFotos(fotosRequest);

        List<FotoEvidencia> fotosNoBanco = fotoEvidenciaGateway.buscarPorItemChecklist(itemChecklist.getId());
        Set<Long> idsNoBanco = fotosNoBanco.stream().map(FotoEvidencia::getId).collect(Collectors.toSet());

        Set<Long> idsMantidos = extrairIdsMantidos(fotosRequest, idsNoBanco, itemChecklist.getId());

        deletarFotosRemovidas(itemChecklist, fotosNoBanco, idsMantidos, contexto);
        adicionarFotosNovas(checklistId, itemChecklist, fotosRequest, arquivosPorChave, contexto);
    }

    private void validarContratoFotos(List<FotoEvidenciaRequest> fotosRequest) {
        for (FotoEvidenciaRequest fotoReq : fotosRequest) {
            boolean temId = fotoReq.id() != null;
            boolean temArquivoKey = fotoReq.arquivoKey() != null && !fotoReq.arquivoKey().isBlank();

            if (temId && temArquivoKey) {
                throw new IllegalArgumentException(
                        "Cada foto deve ter apenas um dos campos: id (existente) OU arquivoKey (nova). Não ambos.");
            }
            if (!temId && !temArquivoKey) {
                throw new IllegalArgumentException(
                        "Cada foto deve ter ao menos um dos campos: id (existente) OU arquivoKey (nova).");
            }
        }
    }

    private Set<Long> extrairIdsMantidos(
            List<FotoEvidenciaRequest> fotosRequest,
            Set<Long> idsNoBanco,
            Long itemChecklistId) {

        Set<Long> idsMantidos = fotosRequest.stream()
                .map(FotoEvidenciaRequest::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<Long> idsInvalidos = idsMantidos.stream()
                .filter(id -> !idsNoBanco.contains(id))
                .toList();

        if (!idsInvalidos.isEmpty()) {
            throw new IllegalArgumentException(
                    "Foto(s) informada(s) não pertence(m) ao ItemChecklist "
                            + itemChecklistId + ": " + idsInvalidos);
        }

        return idsMantidos;
    }

    private void deletarFotosRemovidas(
            ItemChecklist itemChecklist,
            List<FotoEvidencia> fotosNoBanco,
            Set<Long> idsMantidos,
            StorageCompensationContext contexto) {

        List<FotoEvidencia> fotosParaDeletar = fotosNoBanco.stream()
                .filter(f -> !idsMantidos.contains(f.getId()))
                .toList();

        if (fotosParaDeletar.isEmpty()) {
            return;
        }

        fotoEvidenciaGateway.deletarPorIds(fotosParaDeletar.stream().map(FotoEvidencia::getId).toList());

        contexto.registrarPathsAntigosParaDeletarAposCommit(
                fotosParaDeletar.stream().map(FotoEvidencia::getPathFoto).toList());

        Set<Long> idsDeletados = fotosParaDeletar.stream().map(FotoEvidencia::getId).collect(Collectors.toSet());
        itemChecklist.getFotosEvidencia().removeIf(f -> idsDeletados.contains(f.getId()));
    }

    private void adicionarFotosNovas(
            Long checklistId,
            ItemChecklist itemChecklist,
            List<FotoEvidenciaRequest> fotosRequest,
            Map<String, MultipartFile> arquivosPorChave,
            StorageCompensationContext contexto) throws IOException {

        for (FotoEvidenciaRequest fotoReq : fotosRequest) {
            String arquivoKey = fotoReq.arquivoKey();
            if (arquivoKey == null || arquivoKey.isBlank()) {
                continue;
            }

            MultipartFile arquivo = arquivosPorChave.get(arquivoKey);
            if (arquivo == null || arquivo.isEmpty()) {
                throw new IllegalArgumentException(
                        "Arquivo de foto não encontrado para a chave: " + arquivoKey);
            }

            String contentType = arquivo.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException(
                        "Arquivo deve ser imagem válida para a chave: " + arquivoKey);
            }

            String pathFoto = storageService.upload(
                    arquivo.getBytes(),
                    "checklists/" + checklistId + "/itens/" + itemChecklist.getId(),
                    contentType);

            contexto.registrarUploadNovo(pathFoto);

            FotoEvidencia fotoNova = fotoEvidenciaGateway.salvar(FotoEvidencia.nova(itemChecklist, pathFoto));
            itemChecklist.adicionarFotoEvidencia(fotoNova);
        }
    }
}
