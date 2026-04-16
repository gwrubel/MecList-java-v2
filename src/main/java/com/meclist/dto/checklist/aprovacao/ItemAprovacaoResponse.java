package com.meclist.dto.checklist.aprovacao;

import java.math.BigDecimal;
import java.util.List;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.StatusItem;
import com.meclist.dto.fotoEvidencia.FotoEvidenciaResponse;

public record ItemAprovacaoResponse(
    Long itemChecklistId,
    String nomeDoItem,
    CategoriaParteVeiculo parteDoVeiculo,
    StatusItem statusItem,
    List<FotoEvidenciaResponse> fotos,
    List<ProdutoAprovacaoResponse> produtos,
    BigDecimal maoDeObra
) {}
