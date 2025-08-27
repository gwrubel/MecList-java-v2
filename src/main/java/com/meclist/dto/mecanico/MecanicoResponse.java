package com.meclist.dto.mecanico;

public record MecanicoResponse(
    Long id,
    String nome,
    String email,
    String cpf,
    String telefone,
    String situacao
) {
    
}
