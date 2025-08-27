package com.meclist.dto.parteveiculo;

import org.springframework.web.multipart.MultipartFile;

import com.meclist.domain.enums.CategoriaParteVeiculo;

import jakarta.validation.constraints.NotBlank;

public record ParteVeiculoRequest(
        @NotBlank(message = "O nome da parte é obrigatório") 
        String nome,
        @NotBlank(message = "A descrição da parte é obrigatória")
        CategoriaParteVeiculo categoriaParteVeiculo,
        MultipartFile imagem) {

}
