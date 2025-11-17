package com.meclist.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.meclist.domain.Item;
import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.dto.item.ItemResponse;
import com.meclist.dto.item.ItemsPorCategoriaResponse;
import com.meclist.usecase.item.ListarItensPorCategoriaUseCase;
import com.meclist.usecase.item.ListarTodosItensPorCategoriaUseCase;
import com.meclist.usecase.item.ListarTodosItensUseCase;
import com.meclist.usecase.item.CadastrarItemUseCase;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/itens")
public class ItemController {

    private final ListarItensPorCategoriaUseCase listarItensPorCategoriaUseCase;
    private final ListarTodosItensPorCategoriaUseCase listarTodosItensPorCategoriaUseCase;
    private final ListarTodosItensUseCase listarTodosItensUseCase;
    private final CadastrarItemUseCase cadastrarItemUseCase;

    public ItemController(ListarItensPorCategoriaUseCase listarItensPorCategoriaUseCase,
            ListarTodosItensPorCategoriaUseCase listarTodosItensPorCategoriaUseCase,
            ListarTodosItensUseCase listarTodosItensUseCase,
            CadastrarItemUseCase cadastrarItemUseCase) {
        this.listarItensPorCategoriaUseCase = listarItensPorCategoriaUseCase;
        this.listarTodosItensPorCategoriaUseCase = listarTodosItensPorCategoriaUseCase;
        this.listarTodosItensUseCase = listarTodosItensUseCase;
        this.cadastrarItemUseCase = cadastrarItemUseCase;
    }

    @GetMapping
    public List<ItemResponse> listarPorCategoria(@RequestParam(required = false) CategoriaParteVeiculo categoria) {
        if (categoria != null) {
            return listarItensPorCategoriaUseCase.executar(categoria);
        } else {
            // Se não for especificada uma categoria, retorna todos os itens
            return listarTodosItensUseCase.executar();
        }
    }

    @GetMapping("/agrupados")
    public List<ItemsPorCategoriaResponse> listarTodosItensPorCategoria() {
        return listarTodosItensPorCategoriaUseCase.executar();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> cadastrarItem(
            @RequestParam String nome,
            @RequestParam CategoriaParteVeiculo parteDoVeiculo,
            @RequestPart("imagem") MultipartFile imagem,
            HttpServletRequest servletRequest) {

        System.out.println("=== DEBUG MULTIPART ===");
        System.out.println("Content-Type: " + servletRequest.getContentType());
        System.out.println("Nome: " + nome);
        System.out.println("Categoria: " + parteDoVeiculo);
        System.out.println("Imagem: " + (imagem != null ? imagem.getOriginalFilename() : "null"));
        System.out.println("=======================");

        // Validações básicas
        if (nome == null || nome.trim().isEmpty()) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("timestamp", LocalDateTime.now());
            erro.put("status", HttpStatus.BAD_REQUEST.value());
            erro.put("error", "ValidationError");
            erro.put("message", "Nome do item é obrigatório");
            erro.put("path", servletRequest.getRequestURI());
            return ResponseEntity.badRequest().body(erro);
        }

        if (parteDoVeiculo == null) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("timestamp", LocalDateTime.now());
            erro.put("status", HttpStatus.BAD_REQUEST.value());
            erro.put("error", "ValidationError");
            erro.put("message", "Categoria do veículo é obrigatória");
            erro.put("path", servletRequest.getRequestURI());
            return ResponseEntity.badRequest().body(erro);
        }

        if (imagem == null || imagem.isEmpty()) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("timestamp", LocalDateTime.now());
            erro.put("status", HttpStatus.BAD_REQUEST.value());
            erro.put("error", "ValidationError");
            erro.put("message", "Imagem é obrigatória");
            erro.put("path", servletRequest.getRequestURI());
            return ResponseEntity.badRequest().body(erro);
        }

        try {
            Item item = cadastrarItemUseCase.executar(nome.trim(), parteDoVeiculo, imagem.getBytes());

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("timestamp", LocalDateTime.now());
            resposta.put("status", HttpStatus.CREATED.value());
            resposta.put("message", "Item cadastrado com sucesso!");
            resposta.put("path", servletRequest.getRequestURI());
            resposta.put("data", item);

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> erro = new HashMap<>();
            erro.put("timestamp", LocalDateTime.now());
            erro.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            erro.put("error", "InternalServerError");
            erro.put("message", "Erro interno ao cadastrar item: " + e.getMessage());
            erro.put("path", servletRequest.getRequestURI());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

}
