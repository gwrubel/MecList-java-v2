package com.meclist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import jakarta.persistence.EntityNotFoundException;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "ValidationError");
        response.put("message", ex.getMessage());
        response.put("path", extractPath(request));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "ValidationError");
        response.put("message", "Campos inválidos.");
        response.put("errors", fieldErrors);
        response.put("path", extractPath(request));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CampoInvalidoException.class)
    public ResponseEntity<Object> handleCampoInvalido(CampoInvalidoException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "ValidationError");
        response.put("message", "Campos inválidos.");
        response.put("errors", ex.getErros());
        response.put("path", extractPath(request));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.NOT_FOUND.value()); // Código de status 404
    response.put("error", "EntityNotFound");
    response.put("message", ex.getMessage());
    response.put("path", extractPath(request));

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // Retorna o erro 404
}


    // Método utilitário para limpar a URI
    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }


    @ExceptionHandler(DuplicidadeException.class)
    public ResponseEntity<Object> handleDuplicidade(DuplicidadeException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Duplicidade");
        response.put("message", ex.getMessage());
        response.put("path", extractPath(request));
    
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex, WebRequest request) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Parâmetro obrigatório ausente");
    response.put("message", "O parâmetro '" + ex.getParameterName() + "' é obrigatório.");
    response.put("path", extractPath(request));

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(MissingServletRequestPartException.class)
public ResponseEntity<Object> handleMissingPart(MissingServletRequestPartException ex, WebRequest request) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Parte obrigatória ausente");
    response.put("message", "O campo '" + ex.getRequestPartName() + "' (arquivo) é obrigatório.");
    response.put("path", extractPath(request));

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}

    @ExceptionHandler({java.sql.SQLException.class, org.springframework.dao.DataAccessException.class})
    public ResponseEntity<Object> handleDatabaseExceptions(Exception ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "DatabaseError");
        response.put("message", "Ocorreu um erro interno ao acessar o banco de dados.");
        response.put("path", extractPath(request));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericExceptions(Exception ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "InternalServerError");
        response.put("message", "Ocorreu um erro inesperado.");
        response.put("path", extractPath(request));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
