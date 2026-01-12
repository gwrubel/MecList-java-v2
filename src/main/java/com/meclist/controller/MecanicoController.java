package com.meclist.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.meclist.domain.enums.Situacao;
import com.meclist.dto.adm.AdmRequest;
import com.meclist.dto.mecanico.AtualizarMecanicoRequest;
import com.meclist.dto.mecanico.MecanicoRequest;
import com.meclist.dto.mecanico.MecanicoResponse;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.mecanico.CadastrarMecanicoUseCase;
import com.meclist.usecase.mecanico.AtualizarDadosMecanico;
import com.meclist.usecase.mecanico.AuthMecanicoUseCase;
import com.meclist.usecase.mecanico.BuscarPorSituacaoMecanicoUseCase;
import com.meclist.usecase.mecanico.ListarMecanicosUseCase;


@RestController
@RequestMapping("/mecanicos")
public class MecanicoController extends BaseController {

    private final CadastrarMecanicoUseCase cadastrarMecanicoUseCase;
    private final ListarMecanicosUseCase listarMecanicosUseCase;
    private final BuscarPorSituacaoMecanicoUseCase buscarPorSituacaoMecanicoUseCase;
    private final AtualizarDadosMecanico atualizarDadosMecanicoUseCase;
    private final AuthMecanicoUseCase authMecanicoUseCase;

    public MecanicoController(
            CadastrarMecanicoUseCase cadastrarMecanicoUseCase,
            ListarMecanicosUseCase listarMecanicosUseCase,
            AtualizarDadosMecanico atualizarDadosMecanicoUseCase,
            BuscarPorSituacaoMecanicoUseCase buscarPorSituacaoMecanicoUseCase,
            AuthMecanicoUseCase authMecanicoUseCase) {
        this.listarMecanicosUseCase = listarMecanicosUseCase;
        this.cadastrarMecanicoUseCase = cadastrarMecanicoUseCase;
        this.atualizarDadosMecanicoUseCase = atualizarDadosMecanicoUseCase;
        this.buscarPorSituacaoMecanicoUseCase = buscarPorSituacaoMecanicoUseCase;
        this.authMecanicoUseCase = authMecanicoUseCase;
    }

    /**
     * Cadastra um novo mecânico.
     * 
     * @param request Dados do mecânico a cadastrar
     * @param servletRequest HttpServletRequest
     * @return Resposta padronizada 201 CREATED
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MecanicoResponse>> cadastrar(
            @RequestBody @Valid MecanicoRequest request,
            HttpServletRequest servletRequest) {

        MecanicoResponse mecanico = cadastrarMecanicoUseCase.cadastrarMecanico(request);
        return created("Mecânico cadastrado com sucesso!", mecanico, servletRequest);
    }

    /**
     * Lista mecânicos, opcionalmente filtrados por situação.
     * 
     * @param situacao Situação opcional para filtro
     * @param servletRequest HttpServletRequest
     * @return Lista de mecânicos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MecanicoResponse>>> listarMecanicos(
            @RequestParam(required = false) Situacao situacao,
            HttpServletRequest servletRequest) {
        
        List<MecanicoResponse> mecanicos = situacao != null
            ? buscarPorSituacaoMecanicoUseCase.buscarPorSituacao(situacao)
            : listarMecanicosUseCase.listarTodos();
        
        return success("Mecânicos listados com sucesso!", mecanicos, servletRequest);
    }

    /**
     * Atualiza dados de um mecânico.
     * 
     * @param id ID do mecânico
     * @param request Dados a atualizar
     * @param servletRequest HttpServletRequest
     * @return Resposta padronizada de sucesso
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MecanicoResponse>> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarMecanicoRequest request,
            HttpServletRequest servletRequest) {
        MecanicoResponse mecanico = atualizarDadosMecanicoUseCase.atualizarDados(request, id);
        return updated("Mecânico atualizado com sucesso!", mecanico, servletRequest);
    }

    /**
     * Autentica um mecânico.
     * 
     * @param request Credenciais de login
     * @return Token de autenticação
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody AdmRequest request) {
        String token = authMecanicoUseCase.autenticar(request.email(), request.senha());
        return ResponseEntity.ok(ApiResponse.success("Login realizado com sucesso!", null, token));
    }
}
