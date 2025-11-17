package com.meclist.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meclist.domain.Checklist;
import com.meclist.dto.checklist.CriarChecklistRequest;
import com.meclist.dto.checklist.ItemChecklistResponse;
import com.meclist.usecase.checklist.CriarChecklistUseCase;
import com.meclist.usecase.checklist.ListarItensChecklistUseCase;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/checklists")
public class ChecklistController {

    private final CriarChecklistUseCase criarChecklistUseCase;
    private final ListarItensChecklistUseCase listarItensChecklistUseCase;

    public ChecklistController(CriarChecklistUseCase criarChecklistUseCase,
                              ListarItensChecklistUseCase listarItensChecklistUseCase) {
        this.criarChecklistUseCase = criarChecklistUseCase;
        this.listarItensChecklistUseCase = listarItensChecklistUseCase;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> criarChecklist(
        @RequestBody CriarChecklistRequest request,
        HttpServletRequest servletRequest
    ) {
        Checklist checklist = criarChecklistUseCase.executar(request);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.CREATED.value());
        resposta.put("message", "Checklist criado com sucesso!");
        resposta.put("path", servletRequest.getRequestURI());
        resposta.put("data", checklist);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }
    
    //lista itens que estão no checklist pelo id do checklist
    @GetMapping("/{id}/itens")
    public ResponseEntity<Map<String, Object>> listarItensChecklist(
        @PathVariable Long id,
        HttpServletRequest servletRequest
    ) {
        List<ItemChecklistResponse> itens = listarItensChecklistUseCase.executar(id);
        
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.OK.value());
        resposta.put("path", servletRequest.getRequestURI());
        resposta.put("data", itens);
        
        return ResponseEntity.ok(resposta);
    }
}



