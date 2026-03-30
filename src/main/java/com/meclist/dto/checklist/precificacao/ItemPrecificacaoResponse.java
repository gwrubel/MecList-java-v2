package com.meclist.dto.checklist.precificacao;

import java.math.BigDecimal;
import java.util.List;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.StatusItem;
import com.meclist.dto.fotoEvidencia.FotoEvidenciaResponse;

public record ItemPrecificacaoResponse(
    Long itemChecklistId,
    String nomeDoItem,
    Long itemId,
    CategoriaParteVeiculo parteDoVeiculo,
    StatusItem statusItem,
    List<FotoEvidenciaResponse> fotos,
    List<ProdutoPrecificadoResponse> produtos,
    BigDecimal maoDeObra
) {}
