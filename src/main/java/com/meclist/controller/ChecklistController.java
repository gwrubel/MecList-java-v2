package com.meclist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.checklist.ChecklistResponse;
import com.meclist.dto.checklist.ChecklistResumoResponse;
import com.meclist.dto.checklist.IniciarChecklistRequest;
import com.meclist.dto.checklist.SalvarItensPorCategoriaRequest;
import com.meclist.dto.checklist.precificacao.ChecklistPrecificacaoResponse;
import com.meclist.dto.fotoEvidencia.FotoEvidenciaResponse;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.checklist.AtualizarItensPorCategoriaUseCase;
import com.meclist.usecase.checklist.BuscarChecklistParaPrecificacaoUseCase;
import com.meclist.usecase.checklist.IniciarChecklistUseCase;
import com.meclist.usecase.checklist.ListarChecklistsPorStatusUseCase;
import com.meclist.usecase.fotoEvidencia.BuscarFotosItemChecklistUseCase;
import com.meclist.usecase.checklist.BuscarChecklistPorIdUseCase;
import com.meclist.usecase.checklist.EnviarChecklistParaPrecificacaoUseCase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/checklists")
public class ChecklistController extends BaseController {

        private static final Logger log = LoggerFactory.getLogger(ChecklistController.class);

        private final IniciarChecklistUseCase iniciarChecklistUseCase;
        private final AtualizarItensPorCategoriaUseCase atualizarItensPorCategoriaUseCase;
        private final BuscarChecklistPorIdUseCase buscarChecklistPorIdUseCase;
        private final BuscarFotosItemChecklistUseCase buscarFotosItemChecklistUseCase;
        private final EnviarChecklistParaPrecificacaoUseCase enviarChecklistParaPrecificacaoUseCase;
        private final ListarChecklistsPorStatusUseCase listarChecklistsPorStatusUseCase;
        private final BuscarChecklistParaPrecificacaoUseCase buscarChecklistParaPrecificacaoUseCase;

        public ChecklistController(
                        IniciarChecklistUseCase iniciarChecklistUseCase,
                        AtualizarItensPorCategoriaUseCase atualizarItensPorCategoriaUseCase,
                        BuscarChecklistPorIdUseCase buscarChecklistPorIdUseCase,
                        BuscarFotosItemChecklistUseCase buscarFotosItemChecklistUseCase,
                        EnviarChecklistParaPrecificacaoUseCase enviarChecklistParaPrecificacaoUseCase,
                        ListarChecklistsPorStatusUseCase listarChecklistsPorStatusUseCase,
                        BuscarChecklistParaPrecificacaoUseCase buscarChecklistParaPrecificacaoUseCase) {
                this.iniciarChecklistUseCase = iniciarChecklistUseCase;
                this.atualizarItensPorCategoriaUseCase = atualizarItensPorCategoriaUseCase;
                this.buscarChecklistPorIdUseCase = buscarChecklistPorIdUseCase;
                this.buscarFotosItemChecklistUseCase = buscarFotosItemChecklistUseCase;
                this.enviarChecklistParaPrecificacaoUseCase = enviarChecklistParaPrecificacaoUseCase;
                this.listarChecklistsPorStatusUseCase = listarChecklistsPorStatusUseCase;
                this.buscarChecklistParaPrecificacaoUseCase = buscarChecklistParaPrecificacaoUseCase;
        }

        /**
         * Inicia um novo checklist para um veículo.
         * 
         * Cria um novo checklist e popula automaticamente com todos os itens do
         * catálogo.
         * Cada item começa com status PENDENTE.
         * 
         * @param veiculoId      ID do veículo a ser inspecionado
         * @param request        Dados da inspeção (quilometragem, descrição do
         *                       problema, ID do mecânico)
         * @param servletRequest HttpServletRequest para logs
         * @return ChecklistResponse com itens agrupados por categoria
         * 
         *         Exemplo de uso:
         *         POST /checklists/veiculos/10/iniciar
         *         {
         *         "mecanicoId": 5,
         *         "quilometragem": 50000.0,
         *         "descricaoProblema": "Cliente relatou barulho no motor"
         *         }
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
                                request.descricao());

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

                ChecklistResponse checklistResponse = atualizarItensPorCategoriaUseCase.executar(checklistId,
                                categoriaParteVeiculo, request);
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

        @GetMapping("/{itemChecklistId}/fotos-evidencia")
        public ResponseEntity<ApiResponse<List<FotoEvidenciaResponse>>> buscarFotosDoItem(
                        @PathVariable Long itemChecklistId,
                        HttpServletRequest request) {

                List<FotoEvidenciaResponse> fotos = buscarFotosItemChecklistUseCase.executar(itemChecklistId);
                return success("Fotos do item checklist carregadas com sucesso!", fotos, request);
        }

        @PatchMapping("/{checklistId}/enviar-para-precificacao")
        public ResponseEntity<ApiResponse<Void>> enviarParaPrecificacao(@PathVariable Long checklistId) {
                enviarChecklistParaPrecificacaoUseCase.executar(checklistId);
                return noContent();
        }

        @GetMapping
        public ResponseEntity<ApiResponse<List<ChecklistResumoResponse>>> listarPorStatus(
                        @RequestParam StatusProcesso status,
                        HttpServletRequest servletRequest) {

                List<ChecklistResumoResponse> checklists = listarChecklistsPorStatusUseCase.executar(status);
                return success("Checklists listados com sucesso!", checklists, servletRequest);
        }

        @GetMapping("/{checklistId}/precificacao")
        public ResponseEntity<ApiResponse<ChecklistPrecificacaoResponse>> buscarParaPrecificacao(
                        @PathVariable Long checklistId,
                        HttpServletRequest servletRequest) {

                ChecklistPrecificacaoResponse response = buscarChecklistParaPrecificacaoUseCase.executar(checklistId);
                return success("Checklist carregado para precificação!", response, servletRequest);
        }

}