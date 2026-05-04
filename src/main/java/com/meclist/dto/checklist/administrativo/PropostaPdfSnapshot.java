package com.meclist.dto.checklist.administrativo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PropostaPdfSnapshot(
        Long checklistId,
        String clienteNome,
        String clienteDocumento,
        String clienteTelefone,
        String clienteEndereco,
        String placa,
        String marcaVeiculo,
        String modeloVeiculo,
        Integer anoVeiculo,
        Float quilometragem,
        LocalDate dataEmissao,
        BigDecimal totalMaoDeObra,
        BigDecimal totalProdutos,
        BigDecimal totalGeral,
        List<PropostaPdfItemSnapshot> itens
) {
}