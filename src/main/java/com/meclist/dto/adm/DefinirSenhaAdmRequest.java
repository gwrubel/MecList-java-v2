package com.meclist.dto.adm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DefinirSenhaAdmRequest(
    @NotBlank(message = "O token é obrigatório")
    String token,

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String senha
) {}