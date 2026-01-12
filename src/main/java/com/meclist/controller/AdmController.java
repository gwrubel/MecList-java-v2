package com.meclist.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.meclist.dto.adm.AdmRequest;
import com.meclist.dto.adm.AdmResponse;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.adm.AutenticarAdmUseCase;
import com.meclist.usecase.adm.CadastroAdmUseCase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/adms")
public class AdmController extends BaseController {

    private final CadastroAdmUseCase cadastroAdmUseCase;
    private final AutenticarAdmUseCase autenticarAdmUseCase;

    public AdmController(CadastroAdmUseCase cadastroAdmUseCase,
            AutenticarAdmUseCase autenticarAdmUseCase) {
        this.cadastroAdmUseCase = cadastroAdmUseCase;
        this.autenticarAdmUseCase = autenticarAdmUseCase;
    }

    /**
     * Cadastra um novo administrador.
     * 
     * @param request Dados do administrador (email e senha)
     * @param servletRequest HttpServletRequest para obter informações da requisição
     * @return Resposta de sucesso com status 201 CREATED
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AdmResponse>> cadastrar(
            @RequestBody @Valid AdmRequest request,
            HttpServletRequest servletRequest) {
        
       var adm = cadastroAdmUseCase.cadastrarAdm(request);
        
        return created("Administrador cadastrado com sucesso!", adm, servletRequest);
    }

    /**
     * Autentica um administrador e retorna um token JWT.
     * 
     * @param request Credenciais (email e senha)
     * @param servletRequest HttpServletRequest para obter informações da requisição
     * @return Token JWT encapsulado em ApiResponse
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(
            @RequestBody AdmRequest request,
            HttpServletRequest servletRequest) {
        
        String token = autenticarAdmUseCase.autenticar(request.email(), request.senha());
        
        return success(
            "Autenticação realizada com sucesso!",
            Map.of("token", token),
            servletRequest
        );
    }

}
