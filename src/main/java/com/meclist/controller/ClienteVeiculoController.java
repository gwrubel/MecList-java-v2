package com.meclist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meclist.dto.veiculo.AtualizarVeiculoRequestDTO;
import com.meclist.dto.veiculo.VeiculoRequestDTO;
import com.meclist.dto.veiculo.VeiculoResponse;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.veiculo.AtualizarVeiculoUseCase;
import com.meclist.usecase.veiculo.CadastrarVeiculoUseCase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/clientes/{idCliente}/veiculos")
public class ClienteVeiculoController extends BaseController {

    private final CadastrarVeiculoUseCase cadastrarVeiculoUseCase;
    private final AtualizarVeiculoUseCase atualizarVeiculoUseCase;

    public ClienteVeiculoController(
            CadastrarVeiculoUseCase cadastrarVeiculoUseCase,
            AtualizarVeiculoUseCase atualizarVeiculoUseCase) {
        this.cadastrarVeiculoUseCase = cadastrarVeiculoUseCase;
        this.atualizarVeiculoUseCase = atualizarVeiculoUseCase;
    }

    /**
     * Cadastra um novo veículo para um cliente.
     * 
     * @param idCliente ID do cliente
     * @param request Dados do veículo
     * @param servletRequest HttpServletRequest
     * @return Resposta padronizada 201 CREATED
     */
    @PostMapping
    public ResponseEntity<ApiResponse<VeiculoResponse>> cadastrarVeiculo(
            @PathVariable Long idCliente,
            @Valid @RequestBody VeiculoRequestDTO request,
            HttpServletRequest servletRequest) {
        var veiculo = cadastrarVeiculoUseCase.cadastarVeiculo(idCliente, request);
        return created("Veículo cadastrado com sucesso!", veiculo, servletRequest);
    }

    /**
     * Atualiza um veículo de um cliente.
     * 
     * @param idCliente ID do cliente
     * @param idVeiculo ID do veículo
     * @param request Dados a atualizar
     * @param servletRequest HttpServletRequest
     * @return Resposta padronizada de sucesso
     */
    @PutMapping("/{idVeiculo}")
    public ResponseEntity<ApiResponse<VeiculoResponse>> atualizarVeiculo(
            @PathVariable Long idCliente,
            @PathVariable Long idVeiculo,
            @Valid @RequestBody AtualizarVeiculoRequestDTO request,
            HttpServletRequest servletRequest) {
        var veiculo = atualizarVeiculoUseCase.atualizarVeiculo(idCliente, idVeiculo, request);
        return updated("Veículo atualizado com sucesso!", veiculo, servletRequest);
    }
}
