package com.meclist.dto.itemChecklist;

import java.util.List;

import com.meclist.domain.enums.StatusItem;
import com.meclist.dto.fotoEvidencia.FotoEvidenciaRequest;
import com.meclist.dto.produto.ProdutoAdicionado;

public record AtualizacaoItemChecklist(
    Long itemChecklistId,
    StatusItem statusItem,
    List<FotoEvidenciaRequest> fotos,
    List<ProdutoAdicionado> produtosAdicionados
) {
}
