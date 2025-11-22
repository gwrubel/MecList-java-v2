package com.meclist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.meclist.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Classe base para todos os controllers que padroniza o tratamento de responses.
 * 
 * Benefícios:
 * - Consistência em todas as respostas
 * - DRY (Don't Repeat Yourself)
 * - Fácil manutenção centralizada
 */
@RestController
public abstract class BaseController {
    
    /**
     * Retorna uma resposta de sucesso 200 OK
     */
    protected <T> ResponseEntity<ApiResponse<T>> success(
            String message,
            T data,
            HttpServletRequest request) {
        return ResponseEntity.ok(
            ApiResponse.success(message, request.getRequestURI(), data)
        );
    }
    
    /**
     * Retorna uma resposta de criação 201 CREATED
     */
    protected <T> ResponseEntity<ApiResponse<T>> created(
            String message,
            T data,
            HttpServletRequest request) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(message, request.getRequestURI(), data));
    }
    
    /**
     * Retorna uma resposta de sucesso para atualização
     */
    protected <T> ResponseEntity<ApiResponse<T>> updated(
            String message,
            T data,
            HttpServletRequest request) {
        return ResponseEntity.ok(
            ApiResponse.success(message, request.getRequestURI(), data)
        );
    }
    
    /**
     * Retorna uma resposta de sucesso para deleção
     */
    protected ResponseEntity<ApiResponse<Void>> deleted(
            String message,
            HttpServletRequest request) {
        return ResponseEntity.ok(
            ApiResponse.success(message, request.getRequestURI(), null)
        );
    }
    
    /**
     * Retorna 204 No Content
     */
    protected <T> ResponseEntity<T> noContent() {
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Retorna uma resposta com dados paginados
     */
    protected <T> ResponseEntity<ApiResponse<T>> page(
            String message,
            T pageData,
            HttpServletRequest request) {
        return ResponseEntity.ok(
            ApiResponse.success(message, request.getRequestURI(), pageData)
        );
    }

    /**
     * Retorna uma resposta de erro 400 BAD REQUEST
     */
    protected <T> ResponseEntity<ApiResponse<T>> error(
            Integer code,
            String message,
            String path,
            T data) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(code, message, path, data));
    }
} 
