package com.meclist.dto.servico;

import jakarta.validation.constraints.NotNull;

public record VincularServicoRequest(
        @NotNull Long mecanicoId
) {
}
