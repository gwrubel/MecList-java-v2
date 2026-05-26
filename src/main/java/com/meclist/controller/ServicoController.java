package com.meclist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meclist.dto.servico.ChecklistExecucaoServicoResponse;
import com.meclist.dto.servico.ConcluirServicoResponse;
import com.meclist.dto.servico.IniciarServicoResponse;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.servico.BuscarChecklistExecucaoServicoUseCase;
import com.meclist.usecase.servico.ConcluirServicoUseCase;
import com.meclist.usecase.servico.IniciarServicoUseCase;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/servicos")
public class ServicoController extends BaseController {

    private final BuscarChecklistExecucaoServicoUseCase buscarChecklistExecucaoServicoUseCase;
    private final IniciarServicoUseCase iniciarServicoUseCase;
    private final ConcluirServicoUseCase concluirServicoUseCase;

    public ServicoController(BuscarChecklistExecucaoServicoUseCase buscarChecklistExecucaoServicoUseCase,
                             IniciarServicoUseCase iniciarServicoUseCase,
                             ConcluirServicoUseCase concluirServicoUseCase) {
        this.buscarChecklistExecucaoServicoUseCase = buscarChecklistExecucaoServicoUseCase;
        this.iniciarServicoUseCase = iniciarServicoUseCase;
        this.concluirServicoUseCase = concluirServicoUseCase;
    }

    @GetMapping("/{servicoId}/execucao")
    public ResponseEntity<ApiResponse<ChecklistExecucaoServicoResponse>> buscarChecklistExecucao(
            @PathVariable Long servicoId,
            HttpServletRequest servletRequest) {
        ChecklistExecucaoServicoResponse response = buscarChecklistExecucaoServicoUseCase.executar(servicoId);
        return success("Checklist de execução carregado com sucesso!", response, servletRequest);
    }

    @PatchMapping("/{servicoId}/iniciar")
    public ResponseEntity<ApiResponse<IniciarServicoResponse>> iniciarServico(
            @PathVariable Long servicoId,
            HttpServletRequest servletRequest) {
        IniciarServicoResponse response = iniciarServicoUseCase.executar(servicoId);
        return success("Serviço iniciado com sucesso!", response, servletRequest);
    }

    @PatchMapping("/{servicoId}/concluir")
    public ResponseEntity<ApiResponse<ConcluirServicoResponse>> concluirServico(
            @PathVariable Long servicoId,
            HttpServletRequest servletRequest) {
        ConcluirServicoResponse response = concluirServicoUseCase.executar(servicoId);
        return success("Serviço concluído com sucesso!", response, servletRequest);
    }
}
