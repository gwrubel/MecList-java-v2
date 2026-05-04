package com.meclist.controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meclist.dto.checklist.administrativo.RegistrarConfirmacaoManualRequest;
import com.meclist.dto.checklist.aprovacao.AprovarChecklistRequest;
import com.meclist.dto.checklist.aprovacao.ChecklistAprovacaoResponse;
import com.meclist.dto.checklist.aprovacaoLink.GerarLinkAprovacaoResponse;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.checklist.AprovarChecklistAdministrativoUseCase;
import com.meclist.usecase.checklist.BuscarChecklistParaAprovacaoAdministrativaUseCase;
import com.meclist.usecase.checklist.GerarLinkAprovacaoClienteUseCase;
import com.meclist.usecase.checklist.GerarPropostaPdfUseCase;
import com.meclist.usecase.checklist.IniciarFluxoManualAprovacaoUseCase;
import com.meclist.usecase.checklist.RegistrarConfirmacaoManualUseCase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/checklists")
public class ChecklistAdministrativoController extends BaseController {

    private final BuscarChecklistParaAprovacaoAdministrativaUseCase buscarChecklistParaAprovacaoAdministrativaUseCase;
    private final IniciarFluxoManualAprovacaoUseCase iniciarFluxoManualAprovacaoUseCase;
    private final GerarPropostaPdfUseCase gerarPropostaPdfUseCase;
    private final RegistrarConfirmacaoManualUseCase registrarConfirmacaoManualUseCase;
    private final AprovarChecklistAdministrativoUseCase aprovarChecklistAdministrativoUseCase;
    private final GerarLinkAprovacaoClienteUseCase gerarLinkAprovacaoClienteUseCase;

    public ChecklistAdministrativoController(
            BuscarChecklistParaAprovacaoAdministrativaUseCase buscarChecklistParaAprovacaoAdministrativaUseCase,
            IniciarFluxoManualAprovacaoUseCase iniciarFluxoManualAprovacaoUseCase,
            GerarPropostaPdfUseCase gerarPropostaPdfUseCase,
            RegistrarConfirmacaoManualUseCase registrarConfirmacaoManualUseCase,
            AprovarChecklistAdministrativoUseCase aprovarChecklistAdministrativoUseCase,
            GerarLinkAprovacaoClienteUseCase gerarLinkAprovacaoClienteUseCase) {
        this.buscarChecklistParaAprovacaoAdministrativaUseCase = buscarChecklistParaAprovacaoAdministrativaUseCase;
        this.iniciarFluxoManualAprovacaoUseCase = iniciarFluxoManualAprovacaoUseCase;
        this.gerarPropostaPdfUseCase = gerarPropostaPdfUseCase;
        this.registrarConfirmacaoManualUseCase = registrarConfirmacaoManualUseCase;
        this.aprovarChecklistAdministrativoUseCase = aprovarChecklistAdministrativoUseCase;
        this.gerarLinkAprovacaoClienteUseCase = gerarLinkAprovacaoClienteUseCase;
    }

    @GetMapping("/{checklistId}/aprovacao")
    public ResponseEntity<ApiResponse<ChecklistAprovacaoResponse>> buscarParaAprovacaoAdministrativa(
            @PathVariable Long checklistId,
            HttpServletRequest request) {
        ChecklistAprovacaoResponse response = buscarChecklistParaAprovacaoAdministrativaUseCase.executar(checklistId);
        return success("Checklist carregado para aprovação administrativa!", response, request);
    }

    @PatchMapping("/{checklistId}/fluxo-manual/iniciar")
    public ResponseEntity<Void> iniciarFluxoManual(@PathVariable Long checklistId) {
        iniciarFluxoManualAprovacaoUseCase.executar(checklistId);
        return noContent();
    }

    @GetMapping(value = "/{checklistId}/fluxo-manual/proposta.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerarPropostaPdf(@PathVariable Long checklistId) {
        byte[] pdf = gerarPropostaPdfUseCase.executar(checklistId);
        String filename = "proposta-checklist-" + checklistId + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename(filename).build().toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping("/{checklistId}/fluxo-manual/confirmacao")
    public ResponseEntity<Void> registrarConfirmacaoManual(
            @PathVariable Long checklistId,
            @Valid @RequestBody RegistrarConfirmacaoManualRequest request) {
        registrarConfirmacaoManualUseCase.executar(checklistId, request);
        return noContent();
    }

    @PostMapping("/{checklistId}/fluxo-manual/aprovar")
    public ResponseEntity<Void> aprovarAdministrativamente(
            @PathVariable Long checklistId,
            @Valid @RequestBody AprovarChecklistRequest request) {
        aprovarChecklistAdministrativoUseCase.executar(checklistId, request);
        return noContent();
    }

    @PostMapping("/{checklistId}/link-aprovacao-cliente")
    public ResponseEntity<ApiResponse<GerarLinkAprovacaoResponse>> gerarLinkAprovacaoCliente(
            @PathVariable Long checklistId,
            HttpServletRequest request) {
        GerarLinkAprovacaoResponse response = gerarLinkAprovacaoClienteUseCase.executar(checklistId);
        return success("Link seguro de aprovação gerado e enviado ao cliente.", response, request);
    }
}