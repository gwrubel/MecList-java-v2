package com.meclist.dto.mecanico;

import com.meclist.domain.enums.Situacao;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MecanicoRequest(
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    String nome,
    
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 255, message = "O e-mail deve ter no máximo 255 caracteres")
    String email,
    
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 128, message = "A senha deve ter entre 6 e 128 caracteres")
    String senha,

    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", 
             message = "Telefone inválido! Use o formato: (XX) XXXXX-XXXX ou (XX) XXXX-XXXX") 
    String telefone,
    
    @NotBlank(message = "O CPF é obrigatório")
    String cpf,
    
    Situacao situacao
    
) {
    
    /**
     * Retorna o CPF limpo (apenas números)
     */
    public String cpfLimpo() {
        return cpf != null ? cpf.replaceAll("\\D", "") : null;
    }
    
    /**
     * Verifica se o CPF é válido
     */
    public boolean isCpfValido() {
        return com.meclist.validator.ValidatorUsuario.isCpfValido(cpf);
    }
    
    /**
     * Retorna o CPF formatado para exibição
     */
    public String cpfFormatado() {
        return com.meclist.validator.ValidatorUsuario.formatarCpf(cpf);
    }
}
