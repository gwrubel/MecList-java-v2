package com.meclist.dto.cliente;

import com.meclist.domain.enums.Situacao;
import com.meclist.domain.enums.TipoDocumento;
import com.meclist.domain.enums.TipoDeUsuario;
import com.meclist.dto.veiculo.VeiculoResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ClienteResponse(
    Long id,
    String nome,
    String email,
    String documento,
    TipoDocumento tipoDocumento,
    String telefone,
    String endereco,
    Situacao situacao,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm,
    List<VeiculoResponse> veiculos
) {}
