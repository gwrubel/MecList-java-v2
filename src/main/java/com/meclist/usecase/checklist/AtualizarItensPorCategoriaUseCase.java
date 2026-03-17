package com.meclist.usecase.checklist;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.meclist.domain.Checklist;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.Produto;
import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.dto.checklist.ChecklistResponse;
import com.meclist.dto.checklist.SalvarItensPorCategoriaRequest;
import com.meclist.dto.itemChecklist.AtualizacaoItemChecklist;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.exception.ItemNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ItemChecklistGateway;
import com.meclist.interfaces.ProdutoGateway;
import com.meclist.interfaces.StorageService;
import com.meclist.mapper.ChecklistMapper;

import jakarta.persistence.EntityManager;

@Service
public class AtualizarItensPorCategoriaUseCase {
    private final ChecklistGateway checklistGateway;
    private final ItemChecklistGateway itemChecklistGateway;
    private final ProdutoGateway produtoGateway;
    private final StorageService storageService;
    private final EntityManager entityManager;

    private final ItemChecklistCategoriaValidator itemChecklistCategoriaValidator;
    private final ProdutoChecklistUpdater produtoChecklistUpdater;
    private final FotoEvidenciaUpdater fotoEvidenciaUpdater;

    private final ChecklistWorkflowGuard checklistWorkflowGuard;

    public AtualizarItensPorCategoriaUseCase(
            ChecklistGateway checklistGateway,
            ItemChecklistGateway itemChecklistGateway,
            ProdutoGateway produtoGateway,
            StorageService storageService,
            EntityManager entityManager,
            ItemChecklistCategoriaValidator itemChecklistCategoriaValidator,
            ProdutoChecklistUpdater produtoChecklistUpdater,
            FotoEvidenciaUpdater fotoEvidenciaUpdater,
            ChecklistWorkflowGuard checklistWorkflowGuard) {
        this.checklistGateway = checklistGateway;
        this.itemChecklistGateway = itemChecklistGateway;
        this.produtoGateway = produtoGateway;
        this.storageService = storageService;
        this.entityManager = entityManager;
        this.itemChecklistCategoriaValidator = itemChecklistCategoriaValidator;
        this.produtoChecklistUpdater = produtoChecklistUpdater;
        this.fotoEvidenciaUpdater = fotoEvidenciaUpdater;
        this.checklistWorkflowGuard = checklistWorkflowGuard;

    }

    public ChecklistResponse executar(Long checklistId, CategoriaParteVeiculo categoria, SalvarItensPorCategoriaRequest request) {
        return executar(checklistId, categoria, request, Collections.emptyMap());
    }

    @Transactional
    public ChecklistResponse executar(Long checklistId, CategoriaParteVeiculo categoria,
            SalvarItensPorCategoriaRequest request, Map<String, MultipartFile> arquivosPorChave) {

        Checklist checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException("Checklist não encontrado: " + checklistId));

        checklistWorkflowGuard.validarEdicaoItensPorMecanico(checklist);

        StorageCompensationContext compensationContext = new StorageCompensationContext();

        try {
            Map<Long, Produto> produtosMap = carregarProdutosDoRequest(request);

            for (AtualizacaoItemChecklist itemAtualizacao : request.itens()) {
                ItemChecklist itemChecklist = itemChecklistGateway
                        .buscarPorId(itemAtualizacao.itemChecklistId())
                        .orElseThrow(() -> new ItemNaoEncontradoException(
                                "ItemChecklist não encontrado: " + itemAtualizacao.itemChecklistId()));

                itemChecklistCategoriaValidator.validar(itemChecklist, checklistId, categoria);

                itemChecklist.atualizarStatus(itemAtualizacao.statusItem());

                fotoEvidenciaUpdater.processarFotos(
                        checklistId,
                        itemChecklist,
                        itemAtualizacao.fotos(),
                        arquivosPorChave,
                        compensationContext);

                produtoChecklistUpdater.atualizarProdutos(
                        itemChecklist,
                        itemAtualizacao.produtosAdicionados(),
                        produtosMap);

                itemChecklistGateway.salvar(itemChecklist);
            }

            compensationContext.registrarDeleteAposCommit(storageService);

            entityManager.flush();
            entityManager.clear();

            Checklist checklistAtualizado = checklistGateway.buscarPorId(checklistId)
                    .orElseThrow(() -> new ChecklistNaoEncontradoException("Checklist não encontrado: " + checklistId));

            return ChecklistMapper.toResponse(checklistAtualizado);

        } catch (IOException e) {
            compensationContext.compensarUploadsNovos(storageService);
            throw new RuntimeException("Erro ao processar upload de fotos.", e);
        } catch (RuntimeException e) {
            compensationContext.compensarUploadsNovos(storageService);
            throw e;
        }
    }

    private Map<Long, Produto> carregarProdutosDoRequest(SalvarItensPorCategoriaRequest request) {
        Set<Long> produtoIds = request.itens().stream()
                .filter(item -> item.produtosAdicionados() != null)
                .flatMap(item -> item.produtosAdicionados().stream())
                .map(p -> p.produtoId())
                .collect(Collectors.toSet());

        if (produtoIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return produtoGateway.buscarPorIds(produtoIds)
                .stream()
                .collect(Collectors.toMap(Produto::getId, p -> p));
    }


}