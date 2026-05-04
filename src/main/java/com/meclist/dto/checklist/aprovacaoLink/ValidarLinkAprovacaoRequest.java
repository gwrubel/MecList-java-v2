package com.meclist.dto.checklist.aprovacaoLink;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ValidarLinkAprovacaoRequest(
        @NotNull Long checklistId,
        @NotBlank String token
) {
}