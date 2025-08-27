package com.meclist.controller;

import com.meclist.dto.veiculo.VeiculoRequestDTO;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.veiculo.AtualizarVeiculoUseCase;
import com.meclist.usecase.veiculo.CadastrarVeiculoUseCase;
import com.meclist.usecase.veiculo.BuscarPorPlacaUseCase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class VeiculoController {

    private final CadastrarVeiculoUseCase cadastrarVeiculoUseCase;
    private final AtualizarVeiculoUseCase atualizarVeiculoUseCase;
    private final BuscarPorPlacaUseCase buscarPorPlacaUseCase;

    public VeiculoController(
            CadastrarVeiculoUseCase cadastrarVeiculoUseCase,
            AtualizarVeiculoUseCase atualizarVeiculoUseCase,
            BuscarPorPlacaUseCase buscarPorPlacaUseCase) {
        this.cadastrarVeiculoUseCase = cadastrarVeiculoUseCase;
        this.atualizarVeiculoUseCase = atualizarVeiculoUseCase;
        this.buscarPorPlacaUseCase = buscarPorPlacaUseCase;
    }

    // Endpoints para veículos de um cliente (rota aninhada)
    @PostMapping("/clientes/{idCliente}/veiculos")
    public ResponseEntity<ApiResponse<Void>> adicionarVeiculo(
            @PathVariable Long idCliente,
            @Valid @RequestBody VeiculoRequestDTO request,
            HttpServletRequest servletRequest) {
        cadastrarVeiculoUseCase.cadastarVeiculo(idCliente, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("Veículo cadastrado com sucesso!", servletRequest.getRequestURI(), null));
    }

    @PutMapping("/clientes/{idCliente}/veiculos/{idVeiculo}")
    public ResponseEntity<ApiResponse<Void>> atualizarVeiculo(
            @PathVariable Long idCliente,
            @PathVariable Long idVeiculo,
            @Valid @RequestBody VeiculoRequestDTO request,
            HttpServletRequest servletRequest) {
        atualizarVeiculoUseCase.atualizarVeiculo(idCliente, idVeiculo, request);
        return ResponseEntity
                .ok(ApiResponse.success("Veículo atualizado com sucesso!", servletRequest.getRequestURI(), null));
    }

    // Endpoint para buscar placas (busca simples, geral)
    @GetMapping("/veiculos/placas")
    public ResponseEntity<List<String>> buscarPlacas(@RequestParam String busca) {
        List<String> placas = buscarPorPlacaUseCase.executar(busca);
        return ResponseEntity.ok(placas);
    }

    @GetMapping("/veiculos/{placa}")
    public ResponseEntity<ApiResponse<?>> buscarPorPlaca(@PathVariable String placa) {
        var veiculoResponse = buscarPorPlacaUseCase.buscarPorPlaca(placa);
        return ResponseEntity.ok(ApiResponse.success("Veículo encontrado com sucesso!", null, veiculoResponse));
    }
}
