package com.meclist.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.dto.checklist.ChecklistResponse;
import com.meclist.dto.checklist.IniciarChecklistRequest;
import com.meclist.dto.checklist.SalvarItensPorCategoriaRequest;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.checklist.AtualizarItensPorCategoriaUseCase;
import com.meclist.usecase.checklist.IniciarChecklistUseCase;
import com.meclist.usecase.checklist.BuscarChecklistPorIdUseCase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/checklists")
public class ChecklistController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ChecklistController.class);

    private final IniciarChecklistUseCase iniciarChecklistUseCase;
    private final AtualizarItensPorCategoriaUseCase atualizarItensPorCategoriaUseCase;
    private final BuscarChecklistPorIdUseCase buscarChecklistPorIdUseCase;

    public ChecklistController(
            IniciarChecklistUseCase iniciarChecklistUseCase,
            AtualizarItensPorCategoriaUseCase atualizarItensPorCategoriaUseCase,
            BuscarChecklistPorIdUseCase buscarChecklistPorIdUseCase) {
        this.iniciarChecklistUseCase = iniciarChecklistUseCase;
        this.atualizarItensPorCategoriaUseCase = atualizarItensPorCategoriaUseCase;
        this.buscarChecklistPorIdUseCase = buscarChecklistPorIdUseCase;
    }

    /**
     * Inicia um novo checklist para um veículo.
     * 
     * Cria um novo checklist e popula automaticamente com todos os itens do catálogo.
     * Cada item começa com status PENDENTE.
     * 
     * @param veiculoId ID do veículo a ser inspecionado
     * @param request Dados da inspeção (quilometragem, descrição do problema, ID do mecânico)
     * @param servletRequest HttpServletRequest para logs
     * @return ChecklistResponse com itens agrupados por categoria
     * 
     * Exemplo de uso:
     * POST /checklists/veiculos/10/iniciar
     * {
     *   "mecanicoId": 5,
     *   "quilometragem": 50000.0,
     *   "descricaoProblema": "Cliente relatou barulho no motor"
     * }
     */
    @PostMapping("/veiculos/{idVeiculo}/iniciar")
    public ResponseEntity<ApiResponse<ChecklistResponse>> iniciarChecklist(
            @PathVariable Long idVeiculo,
            @Valid @RequestBody IniciarChecklistRequest request,
            HttpServletRequest servletRequest) {

        log.debug("Iniciando checklist: veiculoId={}, mecanicoId={}, quilometragem={}", 
                  idVeiculo, request.idMecanico(), request.quilometragem());

        // Seta o veiculoId do request (vem da URL, não do body)
        IniciarChecklistRequest requestComVeiculo = new IniciarChecklistRequest(
                idVeiculo,
                request.idMecanico(),
                request.quilometragem(),
                request.descricao()
        );

        ChecklistResponse checklist = iniciarChecklistUseCase.executar(requestComVeiculo);

        log.info("Checklist iniciado com sucesso: id={}, veiculoId={}, itens={}", 
                 checklist.checklistId(), idVeiculo, checklist.itensPorCategoria().size());

        return created("Checklist iniciado com sucesso!", checklist, servletRequest);
    }


    @PatchMapping(value = "/{checklistId}/categorias/{categoriaParteVeiculo}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ChecklistResponse>> atualizarCategoriaChecklist(
            @PathVariable Long checklistId,
            @PathVariable CategoriaParteVeiculo categoriaParteVeiculo,
            @Valid @RequestBody SalvarItensPorCategoriaRequest request,
             HttpServletRequest servletRequest) {

            ChecklistResponse checklistResponse = atualizarItensPorCategoriaUseCase.executar(checklistId, categoriaParteVeiculo, request);
            log.info("Itens da categoria {} atualizados com sucesso no checklist: id={}", 
                     categoriaParteVeiculo, checklistId);

            return updated("Itens atualizados com sucesso!", checklistResponse, servletRequest);
    }

            @PatchMapping(value = "/{checklistId}/categorias/{categoriaParteVeiculo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
            public ResponseEntity<ApiResponse<ChecklistResponse>> atualizarCategoriaChecklistMultipart(
                @PathVariable Long checklistId,
                @PathVariable CategoriaParteVeiculo categoriaParteVeiculo,
                @Valid @RequestPart("dados") SalvarItensPorCategoriaRequest request,
                MultipartHttpServletRequest servletRequest) {

            ChecklistResponse checklistResponse = atualizarItensPorCategoriaUseCase.executar(
                checklistId,
                categoriaParteVeiculo,
                request,
                servletRequest.getFileMap());

            log.info("Itens da categoria {} atualizados com sucesso no checklist (multipart): id={}, arquivos={}",
                categoriaParteVeiculo,
                checklistId,
                servletRequest.getFileMap().size());

            return updated("Itens atualizados com sucesso!", checklistResponse, servletRequest);
            }

    @GetMapping("/{checklistId}")
    public ResponseEntity<ApiResponse<ChecklistResponse>> buscarPorId(
            @PathVariable Long checklistId,
            HttpServletRequest servletRequest) {

        ChecklistResponse checklist = buscarChecklistPorIdUseCase.executar(checklistId);
        return success("Checklist encontrado com sucesso!", checklist, servletRequest);
    }
}