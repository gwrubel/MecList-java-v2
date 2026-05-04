package com.meclist.dto.checklist.aprovacaoLink;

import java.time.LocalDateTime;

public record GerarLinkAprovacaoResponse(
        Long checklistId,
        String link,
        LocalDateTime expiraEm
) {
}