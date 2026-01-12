package com.meclist.dto.mecanico;

import com.meclist.domain.enums.Situacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MecanicoRequest(
    @NotBlank(message = "O nome é obrigatório")
    String nome,
    
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    String email,
    
   
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String senha,

    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}" , message = "Telefone inválido! Deve conter 10 ou 11 dígitos") 
    String telefone,
    
    @NotBlank(message = "O CPF é obrigatório")
    String cpf,
    
    Situacao situacao
    
) {
    
}
