package com.meclist.dto.admin;

public record DashboardEstadoAtualDTO(
        Long pendentesAtuais,
        Long aguardandoAprovacao,
        Long atribuidos,
        Long emAndamento
) {}
