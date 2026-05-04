package com.meclist.dto.checklist.administrativo;

import com.meclist.domain.enums.CanalConfirmacaoCliente;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrarConfirmacaoManualRequest(
        @NotNull CanalConfirmacaoCliente canalConfirmacao,
        @Size(max = 1000) String observacao
) {
}