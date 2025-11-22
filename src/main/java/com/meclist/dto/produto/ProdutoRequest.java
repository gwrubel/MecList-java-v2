package com.meclist.dto.produto;

import jakarta.validation.constraints.NotBlank;

public record ProdutoRequest(
        @NotBlank(message = "O nome do produto é obrigatório") String nomeProduto

) {

}
