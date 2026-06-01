package com.meclist.dto.adm;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecuperarSenhaAdmRequest(
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    String email
) {}