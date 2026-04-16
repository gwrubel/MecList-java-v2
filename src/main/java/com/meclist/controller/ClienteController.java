package com.meclist.controller;


import com.meclist.domain.enums.Situacao;
import com.meclist.dto.cliente.AtualizarClienteRequest;
import com.meclist.dto.cliente.ChecklistCardResumo;
import com.meclist.dto.cliente.ClienteListResponse;
import com.meclist.dto.cliente.ClienteRequest;
import com.meclist.dto.cliente.ClienteResponse;
import com.meclist.dto.cliente.DashboardClienteResponse;
import com.meclist.dto.cliente.DefinirSenhaRequest;
import com.meclist.dto.cliente.LoginRequest;
import com.meclist.dto.cliente.PrimeiroAcessoRequest;
import com.meclist.dto.checklist.aprovacao.ChecklistAprovacaoResponse;
import com.meclist.response.ApiResponse;
import com.meclist.security.AuthenticatedUser;
import com.meclist.usecase.cliente.AtualizarDadosClienteUseCase;
import com.meclist.usecase.cliente.AutenticarClienteUseCase;
import com.meclist.usecase.cliente.BuscarDadosDashboardUseCase;
import com.meclist.usecase.cliente.BuscarDadosDoClienteUseCase;
import com.meclist.usecase.cliente.BuscarDetalhesChecklistUseCase;
import com.meclist.usecase.cliente.BuscarPorSituacaoUseCase;
import com.meclist.usecase.cliente.CadastrarClienteUseCase;
import com.meclist.usecase.cliente.DefinirSenhaClienteUseCase;
import com.meclist.usecase.cliente.ListarClientesUseCase;
import com.meclist.usecase.cliente.SolicitarPrimeiroAcessoUseCase;
import com.meclist.usecase.checklist.BuscarChecklistParaAprovacaoUseCase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
public class ClienteController extends BaseController {

    private final CadastrarClienteUseCase cadastrarClienteUseCase;
    private final ListarClientesUseCase listarClientesUseCase;
    private final AtualizarDadosClienteUseCase atualizarDadosClienteUseCase;
    private final BuscarPorSituacaoUseCase buscarPorSituacaoUseCase;
    private final BuscarDadosDoClienteUseCase buscarDadosDoClienteUseCase;
    private final DefinirSenhaClienteUseCase definirSenhaClienteUseCase;
    private final SolicitarPrimeiroAcessoUseCase solicitarPrimeiroAcessoUseCase;
    private final AutenticarClienteUseCase autenticarClienteUseCase;
    private final BuscarDadosDashboardUseCase buscarDadosDashboardUseCase;
    private final BuscarChecklistParaAprovacaoUseCase buscarChecklistParaAprovacaoUseCase;
    private final BuscarDetalhesChecklistUseCase buscarDetalhesChecklistUseCase;

    public ClienteController(
            CadastrarClienteUseCase cadastrarClienteUseCase,
            ListarClientesUseCase listarClientesUseCase,
            AtualizarDadosClienteUseCase atualizarDadosClienteUseCase,
            BuscarPorSituacaoUseCase buscarPorSituacaoUseCase,
            BuscarDadosDoClienteUseCase buscarDadosDoClienteUseCase,
            DefinirSenhaClienteUseCase definirSenhaClienteUseCase,
            SolicitarPrimeiroAcessoUseCase solicitarPrimeiroAcessoUseCase,
            AutenticarClienteUseCase autenticarClienteUseCase,
            BuscarDadosDashboardUseCase buscarDadosDashboardUseCase,
            BuscarChecklistParaAprovacaoUseCase buscarChecklistParaAprovacaoUseCase ,
            BuscarDetalhesChecklistUseCase buscarDetalhesChecklistUseCase) {
        this.cadastrarClienteUseCase = cadastrarClienteUseCase;
        this.listarClientesUseCase = listarClientesUseCase;
        this.atualizarDadosClienteUseCase = atualizarDadosClienteUseCase;
        this.buscarPorSituacaoUseCase = buscarPorSituacaoUseCase;
        this.buscarDadosDoClienteUseCase = buscarDadosDoClienteUseCase;
        this.definirSenhaClienteUseCase = definirSenhaClienteUseCase;
        this.solicitarPrimeiroAcessoUseCase = solicitarPrimeiroAcessoUseCase;
        this.autenticarClienteUseCase = autenticarClienteUseCase;
        this.buscarDadosDashboardUseCase = buscarDadosDashboardUseCase;
        this.buscarChecklistParaAprovacaoUseCase = buscarChecklistParaAprovacaoUseCase;
        this.buscarDetalhesChecklistUseCase = buscarDetalhesChecklistUseCase;
    }

    /**
     * Cadastra um novo cliente.
     *
     * Request body: `ClienteRequest` (validação via `@Valid`)
     * Retorno: 201 Created com resposta padronizada (`ApiResponse<Void>`).
     *
     * Exemplo de uso:
     * POST /clientes
     * Body: { "nome": "João", "cpf": "000.000.000-00", ... }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponse>> cadastrarCliente(
            @RequestBody @Valid ClienteRequest request,
            HttpServletRequest servletRequest) {

        var cliente = cadastrarClienteUseCase.cadastrarCliente(request);
        return created("Cliente cadastrado com sucesso!", cliente, servletRequest);
    }

    /**
     * Lista clientes.
     *
     * Parâmetros:
     * - `situacao` (opcional): filtra por `Situacao` (ex.: ATIVO, INATIVO)
     *
     * Retorno: 200 OK com `ApiResponse<List<ClienteListResponse>>`.
     * Retorna apenas a quantidade de veículos, não a lista completa, para melhor
     * performance.
     *
     * Observação: aqui mantivemos retorno simples sem paginação; se desejar,
     * é recomendado adicionar `Pageable` e devolver `page(...)`.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteListResponse>>> listarClientes(
            @RequestParam(required = false) Situacao situacao,
            HttpServletRequest request) {

        List<ClienteListResponse> clientes;
        if (situacao != null) {
            clientes = buscarPorSituacaoUseCase.buscarPorSituacao(situacao);
        } else {
            clientes = listarClientesUseCase.listar();
        }

        return success("Clientes listados com sucesso!", clientes, request);
    }

    /**
     * Atualiza os dados de um cliente.
     *
     * PathVariable:
     * - `id` : ID do cliente a ser atualizado
     *
     * Request body: `AtualizarClienteRequest`
     * Retorno: 200 OK com resposta padronizada (`ApiResponse<Void>`).
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> atualizarCliente(
            @RequestBody @Valid AtualizarClienteRequest request,
            @PathVariable Long id,
            HttpServletRequest servletRequest) {

        var cliente = atualizarDadosClienteUseCase.atualizarDados(request, id);
        return updated("Cliente atualizado com sucesso!", cliente, servletRequest);
    }

    /**
     * Busca um cliente por ID.
     *
     * PathVariable:
     * - `id` : ID do cliente
     *
     * Retornos possíveis:
     * - 200 OK com `ApiResponse<ClienteResponse>` quando encontrado
     * - 404 Not Found com `ApiResponse.error(...)` quando não encontrado
     *
     * Observação: A exceção `EntityNotFoundException` é tratada automaticamente
     * pelo `ApiExceptionHandler`, não sendo necessário capturá-la aqui.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> buscarClientePorId(
            @PathVariable Long id,
            HttpServletRequest request) {
        ClienteResponse response = buscarDadosDoClienteUseCase.buscarDadosDoCliente(id);
        return success("Cliente encontrado com sucesso!", response, request);
    }

    @PostMapping("/definir-senha")
    public ResponseEntity<ApiResponse<Void>> definirSenha(
            @RequestBody @Valid DefinirSenhaRequest request,
            HttpServletRequest servletRequest) {
        definirSenhaClienteUseCase.executar(request);
        return success("Senha definida com sucesso!", null, servletRequest);
    }

    @PostMapping("/primeiro-acesso")
    public ResponseEntity<ApiResponse<Void>> primeiroAcesso(
            @RequestBody @Valid PrimeiroAcessoRequest request,
            HttpServletRequest servletRequest) {
        solicitarPrimeiroAcessoUseCase.executar(request);
        return success("E-mail de definição de senha enviado com sucesso!", null, servletRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(
            @RequestBody LoginRequest request,
            HttpServletRequest servletRequest) {

        String token = autenticarClienteUseCase.autenticar(request.email(), request.senha());

        return success(
                "Autenticação realizada com sucesso!",
                Map.of("token", token),
                servletRequest);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardClienteResponse>> buscarDadosDashboard(
            @AuthenticationPrincipal AuthenticatedUser usuario,
            HttpServletRequest request) {

        DashboardClienteResponse response = buscarDadosDashboardUseCase.buscarDados(usuario.id());

        return success("Dados do dashboard obtidos com sucesso!", response, request);
    }

    @GetMapping("/checklists/{checklistId}/aprovacao")
    public ResponseEntity<ApiResponse<ChecklistAprovacaoResponse>> buscarChecklistParaAprovacao(
            @PathVariable Long checklistId,
            HttpServletRequest request) {

        ChecklistAprovacaoResponse response = buscarChecklistParaAprovacaoUseCase.executar(checklistId);
        return success("Checklist carregado para aprovação!", response, request);
    }

    @GetMapping("/checklists/detalhes/{checklistId}")
    public ResponseEntity<ApiResponse<ChecklistCardResumo>> buscarDetalhesChecklist(
            @PathVariable Long checklistId,
            HttpServletRequest request) {

        ChecklistCardResumo response = buscarDetalhesChecklistUseCase.executar(checklistId);
        return success("Detalhes do checklist carregados com sucesso!", response, request);
    }
}
