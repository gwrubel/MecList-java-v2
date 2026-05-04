package com.meclist.dto.checklist.administrativo;

import java.math.BigDecimal;
import java.util.List;

public record PropostaPdfItemSnapshot(
        String categoria,
        String nomeItem,
        BigDecimal maoDeObra,
        BigDecimal totalProdutos,
        List<PropostaPdfProdutoSnapshot> produtos
) {
}