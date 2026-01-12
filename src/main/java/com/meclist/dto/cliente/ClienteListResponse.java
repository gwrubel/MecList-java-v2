package com.meclist.dto.cliente;

import com.meclist.domain.enums.Situacao;
import com.meclist.domain.enums.TipoDocumento;
import com.meclist.domain.enums.TipoDeUsuario;

import java.time.LocalDateTime;

/**
 * DTO simplificado para listagem de clientes.
 * Retorna apenas a quantidade de veículos, não a lista completa.
 */
public record ClienteListResponse(
    Long id,
    String nome,
    String email,
    String documento,
    TipoDocumento tipoDocumento,
    String telefone,
    String endereco,
    TipoDeUsuario tipoDeUsuario,
    Situacao situacao,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm,
    int quantidadeVeiculos
) {
}
