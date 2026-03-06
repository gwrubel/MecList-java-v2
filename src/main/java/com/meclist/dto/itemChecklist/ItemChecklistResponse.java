package com.meclist.dto.itemChecklist;

import java.time.LocalDateTime;
import java.util.List;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.StatusItem;
import com.meclist.dto.fotoEvidencia.FotoEvidenciaResponse;
import com.meclist.dto.produto.ProdutoAdicionado;


public record ItemChecklistResponse(
    Long itemChecklistId,
    Long itemId,
    String nomeDoItem,
    CategoriaParteVeiculo parteDoVeiculo,
    String imagemIlustrativa,
    StatusItem statusItem,
    List<FotoEvidenciaResponse> fotos,
    List<ProdutoAdicionado> produtosAdicionados,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {}
