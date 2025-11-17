package com.meclist.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meclist.domain.enums.Situacao;
import com.meclist.dto.adm.AdmRequest;
import com.meclist.dto.mecanico.AtualizarMecanicoRequest;
import com.meclist.dto.mecanico.MecanicoRequest;
import com.meclist.dto.mecanico.MecanicoResponse;
import com.meclist.usecase.mecanico.CadastrarMecanicoUseCase;
import com.meclist.usecase.mecanico.AtualizarDadosMecanico;
import com.meclist.usecase.mecanico.AuthMecanicoUseCase;
import com.meclist.usecase.mecanico.BuscarPorSituacaoMecanicoUseCase;
import com.meclist.usecase.mecanico.ListarMecanicosUseCase;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/mecanicos")
public class MecanicoController {

    private final CadastrarMecanicoUseCase cadastrarMecanicoUseCase;
    private final ListarMecanicosUseCase listarMecanicosUseCase;
    private final BuscarPorSituacaoMecanicoUseCase buscarPorSituacaoMecanicoUseCase;
    private final AtualizarDadosMecanico atualizarDadosMecanicoUseCase;
    private final AuthMecanicoUseCase authMecanicoUseCase;

    public MecanicoController(CadastrarMecanicoUseCase cadastrarMecanicoUseCase,
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

    @PostMapping
    public ResponseEntity<Map<String, Object>> cadastrar(@RequestBody @Valid MecanicoRequest request,
            HttpServletRequest servletRequest) {
        cadastrarMecanicoUseCase.cadastrarMecanico(request);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.CREATED.value());
        resposta.put("message", "Mecânico cadastrado com sucesso!");
        resposta.put("path", servletRequest.getRequestURI());

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    public List<MecanicoResponse> listarMecanicos(@RequestParam(required = false) Situacao situacao) {
        if (situacao != null) {
            return buscarPorSituacaoMecanicoUseCase.buscarPorSituacao(situacao);
        } else {
            return listarMecanicosUseCase.listarTodos();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarMecanicoRequest request,
            HttpServletRequest servletRequest) {
        atualizarDadosMecanicoUseCase.atualizarDados(request, id);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.OK.value());
        resposta.put("message", "Mecânico atualizado com sucesso!");
        resposta.put("path", servletRequest.getRequestURI());

        return ResponseEntity.ok(resposta);
    }

    

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AdmRequest request) {
        String token = authMecanicoUseCase.autenticar(request.email(), request.senha());
        return ResponseEntity.ok(Map.of("token", token));
    }

}
