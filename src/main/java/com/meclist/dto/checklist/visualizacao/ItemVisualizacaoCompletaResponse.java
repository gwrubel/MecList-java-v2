package com.meclist.dto.checklist.visualizacao;

import java.math.BigDecimal;
import java.util.List;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.StatusItem;
import com.meclist.dto.fotoEvidencia.FotoEvidenciaResponse;

public record ItemVisualizacaoCompletaResponse(
        Long itemChecklistId,
        String nomeDoItem,
        CategoriaParteVeiculo parteDoVeiculo,
        String imagemIlustrativa,
        StatusItem statusItem,
        List<FotoEvidenciaResponse> fotos,
        List<ProdutoVisualizacaoCompletaResponse> produtos,
        BigDecimal maoDeObra
) {
}
