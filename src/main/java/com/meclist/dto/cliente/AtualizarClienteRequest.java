package com.meclist.dto.cliente;

import com.meclist.domain.enums.Situacao;
import com.meclist.domain.enums.TipoDocumento;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AtualizarClienteRequest(
    
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    String nome,

    @Email(message = "E-mail inválido")
    @Size(max = 255, message = "O e-mail deve ter no máximo 255 caracteres")
    String email,

    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", 
             message = "Telefone inválido! Use o formato: (XX) XXXXX-XXXX ou (XX) XXXX-XXXX")
    String telefone,

    String documento,

    TipoDocumento tipoDocumento,
    
    @Size(min = 5, max = 255, message = "O endereço deve ter entre 5 e 255 caracteres")
    String endereco,
    
    Situacao situacao
) {
    
    /**
     * Retorna o documento limpo (apenas números)
     */
    public String documentoLimpo() {
        return documento != null ? documento.replaceAll("\\D", "") : null;
    }
    
    /**
     * Valida se o documento corresponde ao tipo especificado
     */
    public boolean documentoCompativelComTipo() {
        if (documento == null || tipoDocumento == null) {
            return false;
        }
        
        String documentoLimpo = documentoLimpo();
        
        switch (tipoDocumento) {
            case CPF:
                return documentoLimpo.length() == 11;
            case CNPJ:
                return documentoLimpo.length() == 14;
            default:
                return false;
        }
    }
    
    /**
     * Verifica se pelo menos um campo foi fornecido para atualização
     */
    public boolean temAlgumCampoParaAtualizar() {
        return nome != null || 
               email != null || 
               telefone != null || 
               documento != null || 
               tipoDocumento != null || 
               endereco != null || 
               situacao != null;
    }
}
