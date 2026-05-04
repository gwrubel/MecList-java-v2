package com.meclist.dto.checklist.administrativo;

import java.math.BigDecimal;

public record PropostaPdfProdutoSnapshot(
        String nomeProduto,
        Integer quantidade,
        BigDecimal valorUnitario,
        String marca,
        BigDecimal valorTotal
) {
}