package com.meclist.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meclist.dto.checklist.ChecklistResponse;
import com.meclist.dto.checklist.IniciarChecklistRequest;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.checklist.IniciarChecklistUseCase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/checklists")
public class ChecklistController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ChecklistController.class);

    private final IniciarChecklistUseCase iniciarChecklistUseCase;

    public ChecklistController(IniciarChecklistUseCase iniciarChecklistUseCase) {
        this.iniciarChecklistUseCase = iniciarChecklistUseCase;
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
                 checklist.id(), idVeiculo, checklist.itensPorCategoria().size());

        return created("Checklist iniciado com sucesso!", checklist, servletRequest);
    }


    @PutMapping("/{checklistId}/categoria/{categoriaParteVeiculo}")
    public ResponseEntity<ApiResponse<ChecklistResponse>> atualizarCategoriaChecklist(
            @PathVariable Long checklistId,
            @PathVariable String categoriaParteVeiculo,
            HttpServletRequest servletRequest) {

        // Implementação futura
        return ResponseEntity.ok().build();
    }
}