package com.meclist.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meclist.dto.fotoEvidencia.FotoEvidenciaResponse;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.fotoEvidencia.BuscarFotosItemChecklistUseCase;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/fotos-evidencia")
public class FotoEvidenciaController extends BaseController {

    private final BuscarFotosItemChecklistUseCase buscarFotosItemChecklistUseCase;

    public FotoEvidenciaController(BuscarFotosItemChecklistUseCase buscarFotosItemChecklistUseCase) {
        this.buscarFotosItemChecklistUseCase = buscarFotosItemChecklistUseCase;
    }

  
}
