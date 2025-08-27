package com.meclist.dto.fotoEvidencia;

import jakarta.validation.constraints.NotNull;

public record FotoEvidenciaRequest(
    @NotNull(message = "O ID do item checklist é obrigatório")
    Long idItemChecklist
) {}
