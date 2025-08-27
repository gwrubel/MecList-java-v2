package com.meclist.dto.cliente;

import com.meclist.domain.enums.Situacao;
import com.meclist.domain.enums.TipoDocumento;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record AtualizarClienteRequest(
    String nome,

    @Email(message = "E-mail inválido")
    String email,

    @Pattern(regexp = "\\d{10,11}", message = "Telefone inválido! Deve conter 10 ou 11 dígitos")
    String telefone,

    String documento,
    TipoDocumento tipoDocumento,
    
    String endereco,
    Situacao situacao
) {
    
}
