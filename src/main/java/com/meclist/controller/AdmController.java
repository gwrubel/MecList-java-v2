package com.meclist.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meclist.dto.adm.AdmRequest;
import com.meclist.usecase.adm.AutenticarAdmUseCase;
import com.meclist.usecase.adm.CadastroAdmUseCase;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/adms")
public class AdmController {

    private final CadastroAdmUseCase cadastroAdmUseCase;
    private final AutenticarAdmUseCase autenticarAdmUseCase;

    public AdmController(CadastroAdmUseCase cadastroAdmUseCase,
            AutenticarAdmUseCase autenticarAdmUseCase) {
        this.cadastroAdmUseCase = cadastroAdmUseCase;
        this.autenticarAdmUseCase = autenticarAdmUseCase;
    }

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody @Valid AdmRequest request) {
        cadastroAdmUseCase.cadastrarAdm(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Administrador cadastrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AdmRequest request) {
        String token = autenticarAdmUseCase.autenticar(request.email(), request.senha());
        return ResponseEntity.ok(Map.of("token", token));
    }

}
