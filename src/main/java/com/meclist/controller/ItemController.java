package com.meclist.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.meclist.domain.Item;
import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.dto.checklist.ItemResponse;
import com.meclist.dto.checklist.ItemsPorCategoriaResponse;
import com.meclist.dto.item.CadastrarItemRequest;
import com.meclist.usecase.checklist.ListarItensPorCategoriaUseCase;
import com.meclist.usecase.checklist.ListarTodosItensPorCategoriaUseCase;
import com.meclist.usecase.item.CadastrarItemUseCase;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/itens")
public class ItemController {

    private final ListarItensPorCategoriaUseCase listarItensPorCategoriaUseCase;
    private final ListarTodosItensPorCategoriaUseCase listarTodosItensPorCategoriaUseCase;
    private final CadastrarItemUseCase cadastrarItemUseCase;

    public ItemController(ListarItensPorCategoriaUseCase listarItensPorCategoriaUseCase,
                         ListarTodosItensPorCategoriaUseCase listarTodosItensPorCategoriaUseCase,
                         CadastrarItemUseCase cadastrarItemUseCase) {
        this.listarItensPorCategoriaUseCase = listarItensPorCategoriaUseCase;
        this.listarTodosItensPorCategoriaUseCase = listarTodosItensPorCategoriaUseCase;
        this.cadastrarItemUseCase = cadastrarItemUseCase;
    }

    @GetMapping
    public List<ItemResponse> listarPorCategoria(@RequestParam(required = false) CategoriaParteVeiculo categoria) {
        if (categoria != null) {
            return listarItensPorCategoriaUseCase.executar(categoria);
        } else {
            // Se não for especificada uma categoria, retorna uma lista vazia
            return List.of();
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
    
        try {
            // Cria o request DTO manualmente
            CadastrarItemRequest request = new CadastrarItemRequest(nome, parteDoVeiculo);
    
            Item item = cadastrarItemUseCase.executar(request, imagem.getBytes());
    
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
            erro.put("status", HttpStatus.BAD_REQUEST.value());
            erro.put("message", "Erro ao cadastrar item: " + e.getMessage());
            erro.put("path", servletRequest.getRequestURI());
    
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
}
