package com.meclist.controller; // ou o pacote de controller que você usa

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categorias-veiculo") 
public class CategoriaController extends BaseController {

    @GetMapping
    public ResponseEntity<ApiResponse<List<String>>> listarCategorias(HttpServletRequest request) {
        // Pega todos os valores do enum e converte para uma lista de Strings
        List<String> categorias = Arrays.stream(CategoriaParteVeiculo.values())
                .map(Enum::name) // .name() retorna o nome do enum como String
                .collect(Collectors.toList());

        return success("Categorias listadas com sucesso!", categorias, request);
    }
}