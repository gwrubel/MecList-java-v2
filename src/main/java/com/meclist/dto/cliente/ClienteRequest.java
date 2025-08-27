package com.meclist.dto.cliente;

import com.meclist.domain.enums.TipoDocumento;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
    
    @NotBlank(message = "O nome é obrigatório")
    String nome,
    

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    String email,
    

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String senha,
    
    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}" , message = "Telefone inválido! Deve conter 10 ou 11 dígitos") 
    String telefone,
    

    @NotBlank(message = "O documento é obrigatório")
    String documento,
    
    @NotNull(message = "O tipo de documento é obrigatório")
    TipoDocumento tipoDocumento,
    
    @NotBlank(message = "O endereço é obrigatório")
    String endereco
) {
    
}
