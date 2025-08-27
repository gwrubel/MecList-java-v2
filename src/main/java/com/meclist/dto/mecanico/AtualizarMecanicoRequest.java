package com.meclist.dto.mecanico;

import com.meclist.domain.enums.Situacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;


public record AtualizarMecanicoRequest(
    String nome,

    @Email(message = "E-mail inválido")
    String email,

    @Pattern(regexp = "\\d{10,11}", message = "Telefone inválido! Deve conter 10 ou 11 dígitos")
    String telefone,

    @Pattern(regexp = "\\d{11}", message = "CPF inválido! Deve conter 11 dígitos")
    String cpf,

    Situacao situacao,
    
    String senha
    
) {
    
}
