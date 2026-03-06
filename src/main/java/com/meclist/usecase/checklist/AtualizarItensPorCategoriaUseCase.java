package com.meclist.usecase.checklist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import com.meclist.domain.Checklist;
import com.meclist.domain.ChecklistProduto;
import com.meclist.domain.FotoEvidencia;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.Produto;
import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.dto.checklist.ChecklistResponse;
import com.meclist.dto.checklist.SalvarItensPorCategoriaRequest;
import com.meclist.dto.fotoEvidencia.FotoEvidenciaRequest;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.exception.ItemNaoEncontradoException;
import com.meclist.exception.ProdutoNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ChecklistProdutoGateway;
import com.meclist.interfaces.FotoEvidenciaGateway;
import com.meclist.interfaces.ItemChecklistGateway;
import com.meclist.interfaces.ProdutoGateway;
import com.meclist.interfaces.StorageService;
import com.meclist.mapper.ChecklistMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;

@Service
public class AtualizarItensPorCategoriaUseCase {
    private final ChecklistGateway checklistGateway;
    private final ItemChecklistGateway itemChecklistGateway;
    private final ProdutoGateway produtoGateway;
    private final ChecklistProdutoGateway checklistProdutoGateway;
    private final FotoEvidenciaGateway fotoEvidenciaGateway;
    private final StorageService storageService;
    private final EntityManager entityManager;

    public AtualizarItensPorCategoriaUseCase(ChecklistGateway checklistGateway,
            ItemChecklistGateway itemChecklistGateway,
            ProdutoGateway produtoGateway,
            ChecklistProdutoGateway checklistProdutoGateway,
            FotoEvidenciaGateway fotoEvidenciaGateway,
            StorageService storageService,
            EntityManager entityManager) {
        this.checklistGateway = checklistGateway;
        this.itemChecklistGateway = itemChecklistGateway;
        this.produtoGateway = produtoGateway;
        this.checklistProdutoGateway = checklistProdutoGateway;
        this.fotoEvidenciaGateway = fotoEvidenciaGateway;
        this.storageService = storageService;
        this.entityManager = entityManager;
    }

    @Transactional
    public ChecklistResponse executar(Long checklistId,
            CategoriaParteVeiculo categoria,
            SalvarItensPorCategoriaRequest request) {
        return executar(checklistId, categoria, request, Collections.emptyMap());
    }

    @Transactional
    public ChecklistResponse executar(Long checklistId,
            CategoriaParteVeiculo categoria,
            SalvarItensPorCategoriaRequest request,
            Map<String, MultipartFile> arquivosPorChave) {

        List<String> pathsUploadados = new ArrayList<>();
        List<String> pathsAntigosParaDeletarAposCommit = new ArrayList<>();

        try {
            // ✅ Coleta todos os IDs de produtos ANTES do loop
            Set<Long> produtoIds = request.itens().stream()
                    .filter(item -> item.produtosAdicionados() != null)
                    .flatMap(item -> item.produtosAdicionados().stream())
                    .map(p -> p.produtoId())
                    .collect(Collectors.toSet());

            // ✅ Busca TODOS os produtos de uma vez (1 query)
            Map<Long, Produto> produtosMap = produtoGateway.buscarPorIds(produtoIds)
                    .stream()
                    .collect(Collectors.toMap(Produto::getId, p -> p));

            // Loop principal
            for (var itemAtualizacao : request.itens()) {
                ItemChecklist itemChecklist = itemChecklistGateway
                        .buscarPorId(itemAtualizacao.itemChecklistId())
                        .orElseThrow(() -> new ItemNaoEncontradoException(
                                "ItemChecklist não encontrado: " + itemAtualizacao.itemChecklistId()));

                // ✅ Validar se o item pertence ao checklist correto
                if (!itemChecklist.getChecklist().getId().equals(checklistId)) {
                    throw new IllegalArgumentException(
                            "ItemChecklist " + itemAtualizacao.itemChecklistId() +
                                    " não pertence ao Checklist " + checklistId);
                }

                // ✅ Validar se o item pertence à categoria correta
                if (!itemChecklist.getItem().getParteDoVeiculo().equals(categoria)) {
                    throw new IllegalArgumentException(
                            "Item " + itemAtualizacao.itemChecklistId() +
                                    " não pertence à categoria " + categoria);
                }

                // ✅ Atualizar status
                itemChecklist.atualizarStatus(itemAtualizacao.statusItem());

                // ✅ TRATAMENTO DE FOTOS
                if (itemAtualizacao.fotos() != null) {
                    processarFotos(
                            checklistId,
                            itemChecklist,
                            itemAtualizacao.fotos(),
                            arquivosPorChave,
                            pathsUploadados,
                            pathsAntigosParaDeletarAposCommit);
                }

                // ✅ TRATAMENTO DE PRODUTOS
                if (itemAtualizacao.produtosAdicionados() != null) {
                    // Buscar produtos antigos do banco
                    List<ChecklistProduto> produtosAntigos = checklistProdutoGateway
                            .buscarPorItemChecklist(itemChecklist.getId());

                    // 2️⃣ Deletar produtos antigos do banco
                    if (!produtosAntigos.isEmpty()) {
                        checklistProdutoGateway.deletarPorIds(
                                produtosAntigos.stream()
                                        .map(ChecklistProduto::getId)
                                        .collect(Collectors.toList()));
                    }

                    // 3️⃣ Limpar produtos em memória
                    itemChecklist.limparProdutosOrcados();

                    // 4️⃣ Se lista estiver vazia, só limpa; se tiver itens, recria
                    if (!itemAtualizacao.produtosAdicionados().isEmpty()) {
                        for (var produtoRequest : itemAtualizacao.produtosAdicionados()) {
                            Produto produto = produtosMap.get(produtoRequest.produtoId());
                            if (produto == null) {
                                throw new ProdutoNaoEncontradoException(
                                        "Produto não encontrado: " + produtoRequest.produtoId());
                            }

                            ChecklistProduto checklistProduto = ChecklistProduto.novo(
                                    itemChecklist,
                                    produto,
                                    produtoRequest.quantidade());

                            ChecklistProduto produtoSalvo = checklistProdutoGateway.salvar(checklistProduto);
                            itemChecklist.adicionarProdutoOrcado(produtoSalvo);
                        }
                    }
                }

                // ✅ Salvar item atualizado
                itemChecklistGateway.salvar(itemChecklist);
            }

            // deletar fotos antigas no storage somente após commit
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    for (String path : pathsAntigosParaDeletarAposCommit) {
                        try {
                            storageService.delete(path);
                        } catch (Exception ignored) {
                        }
                    }
                }
            });

            // Garante que leituras abaixo não usem estado antigo do contexto JPA.
            entityManager.flush();
            entityManager.clear();

            Checklist checklistAtualizado = checklistGateway.buscarPorId(checklistId)
                    .orElseThrow(() -> new ChecklistNaoEncontradoException("Checklist não encontrado: " + checklistId));

            return ChecklistMapper.toResponse(checklistAtualizado);

        } catch (RuntimeException | IOException e) {
            // compensação: remove uploads novos se a transação falhar
            for (String path : pathsUploadados) {
                try {
                    storageService.delete(path);
                } catch (Exception ignored) {
                }
            }
            throw new RuntimeException(e);
        }
    }

    private void processarFotos(
            Long checklistId,
            ItemChecklist itemChecklist,
            List<FotoEvidenciaRequest> fotosRequest,
            Map<String, MultipartFile> arquivosPorChave,
            List<String> pathsUploadados,
            List<String> pathsAntigosParaDeletarAposCommit) throws IOException {
        validarContratoFotos(fotosRequest);

        List<FotoEvidencia> fotosNoBanco = fotoEvidenciaGateway.buscarPorItemChecklist(itemChecklist.getId());
        Set<Long> idsNoBanco = fotosNoBanco.stream()
                .map(FotoEvidencia::getId)
                .collect(Collectors.toSet());

        Set<Long> idsMantidos = extrairIdsMantidos(fotosRequest, idsNoBanco, itemChecklist.getId());

        // agora passa itemChecklist para sincronizar memoria
        deletarFotosRemovidas(itemChecklist, fotosNoBanco, idsMantidos, pathsAntigosParaDeletarAposCommit);

        adicionarFotosNovas(
                checklistId,
                itemChecklist,
                fotosRequest,
                arquivosPorChave,
                pathsUploadados);
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
            List<String> pathsAntigosParaDeletarAposCommit) {

        List<FotoEvidencia> fotosParaDeletar = fotosNoBanco.stream()
                .filter(f -> !idsMantidos.contains(f.getId()))
                .toList();

        if (fotosParaDeletar.isEmpty()) {
            return;
        }

        fotoEvidenciaGateway.deletarPorIds(
                fotosParaDeletar.stream().map(FotoEvidencia::getId).toList());

        pathsAntigosParaDeletarAposCommit.addAll(
                fotosParaDeletar.stream().map(FotoEvidencia::getPathFoto).toList());

        // Sincronizar memória: remover da coleção do itemChecklist
        Set<Long> idsDeletados = fotosParaDeletar.stream()
                .map(FotoEvidencia::getId)
                .collect(Collectors.toSet());
        itemChecklist.getFotosEvidencia().removeIf(f -> idsDeletados.contains(f.getId()));
    }

    private void adicionarFotosNovas(
            Long checklistId,
            ItemChecklist itemChecklist,
            List<FotoEvidenciaRequest> fotosRequest,
            Map<String, MultipartFile> arquivosPorChave,
            List<String> pathsUploadados) throws IOException {

        for (FotoEvidenciaRequest fotoReq : fotosRequest) {
            String arquivoKey = fotoReq.arquivoKey();
            if (arquivoKey == null || arquivoKey.isBlank()) {
                continue; // foto existente (id), já mantida
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
            pathsUploadados.add(pathFoto);

            FotoEvidencia fotoNova = fotoEvidenciaGateway.salvar(
                    FotoEvidencia.nova(itemChecklist, pathFoto));
            itemChecklist.adicionarFotoEvidencia(fotoNova);
        }
    }
}