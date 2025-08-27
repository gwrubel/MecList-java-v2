package com.meclist.controller;

import com.meclist.domain.enums.Situacao;
import com.meclist.dto.cliente.AtualizarClienteRequest;
import com.meclist.dto.cliente.ClienteRequest;
import com.meclist.dto.cliente.ClienteResponse;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.cliente.AtualizarDadosClienteUseCase;
import com.meclist.usecase.cliente.BuscarDadosDoClienteUseCase;
import com.meclist.usecase.cliente.BuscarPorSituacaoUseCase;
import com.meclist.usecase.cliente.CadastrarClienteUseCase;
import com.meclist.usecase.cliente.ListarClientesUseCase;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final CadastrarClienteUseCase cadastrarClienteUseCase;
    private final ListarClientesUseCase listarClientesUseCase;
    private final AtualizarDadosClienteUseCase atualizarDadosClienteUseCase;
    private final BuscarPorSituacaoUseCase buscarPorSituacaoUseCase;
    private final BuscarDadosDoClienteUseCase buscarDadosDoClienteUseCase;

    public ClienteController(
            CadastrarClienteUseCase cadastrarClienteUseCase,
            ListarClientesUseCase listarClientesUseCase,
            AtualizarDadosClienteUseCase atualizarDadosClienteUseCase,
            BuscarPorSituacaoUseCase buscarPorSituacaoUseCase,
            BuscarDadosDoClienteUseCase buscarDadosDoClienteUseCase) {
        this.cadastrarClienteUseCase = cadastrarClienteUseCase;
        this.listarClientesUseCase = listarClientesUseCase;
        this.atualizarDadosClienteUseCase = atualizarDadosClienteUseCase;
        this.buscarPorSituacaoUseCase = buscarPorSituacaoUseCase;
        this.buscarDadosDoClienteUseCase = buscarDadosDoClienteUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> cadastrarCliente(
            @RequestBody @Valid ClienteRequest request,
            HttpServletRequest servletRequest) {
        cadastrarClienteUseCase.cadastrarCliente(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Cliente cadastrado com sucesso!", servletRequest.getRequestURI(), null));
    }

    @GetMapping
    public List<ClienteResponse> listarClientes(@RequestParam(required = false) Situacao situacao) {
        if (situacao != null) {
            return buscarPorSituacaoUseCase.buscarPorSituacao(situacao);
        } else {
            return listarClientesUseCase.executar();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> atualizarCliente(
            @RequestBody @Valid AtualizarClienteRequest request,
            @PathVariable Long id,
            HttpServletRequest servletRequest) {
        atualizarDadosClienteUseCase.atualizarDados(request, id);
        return ResponseEntity
                .ok(ApiResponse.success("Cliente atualizado com sucesso!", servletRequest.getRequestURI(), null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarClientePorId(@PathVariable Long id) {
        try {
            ClienteResponse response = buscarDadosDoClienteUseCase.buscarDadosDoCliente(id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
