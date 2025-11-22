package com.meclist.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meclist.response.ApiResponse;
import com.meclist.usecase.veiculo.BuscarPorPlacaUseCase;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controller para consultas gerais de Veículos.
 * 
 * Endpoints:
 * - GET    /veiculos/placas/busca?termo=ABC         - Busca placas por termo
 * - GET    /veiculos/placa/{placa}                  - Busca veículo por placa específica
 */
@RestController
@RequestMapping("/veiculos")
public class VeiculoController extends BaseController {

    private final BuscarPorPlacaUseCase buscarPorPlacaUseCase;

    public VeiculoController(BuscarPorPlacaUseCase buscarPorPlacaUseCase) {
        this.buscarPorPlacaUseCase = buscarPorPlacaUseCase;
    }

    /**
     * Busca placas de veículos por termo de busca.
     * 
     * @param termo Termo de busca para filtrar placas
     * @param servletRequest HttpServletRequest
     * @return Lista de placas encontradas
     */
    @GetMapping("/placas/busca")
    public ResponseEntity<ApiResponse<List<String>>> buscarPlacasPorTermo(
            @RequestParam String termo,
            HttpServletRequest servletRequest) {
        List<String> placas = buscarPorPlacaUseCase.executar(termo);
        return success("Placas encontradas com sucesso!", placas, servletRequest);
    }

    /**
     * Busca um veículo por placa específica.
     * 
     * @param placa Placa do veículo
     * @param servletRequest HttpServletRequest
     * @return Dados do veículo encontrado
     */
    @GetMapping("/placa/{placa}")
    public ResponseEntity<ApiResponse<Object>> buscarPorPlaca(
            @PathVariable String placa,
            HttpServletRequest servletRequest) {
        var veiculoResponse = buscarPorPlacaUseCase.buscarPorPlaca(placa);
        return success("Veículo encontrado com sucesso!", veiculoResponse, servletRequest);
    }
}

