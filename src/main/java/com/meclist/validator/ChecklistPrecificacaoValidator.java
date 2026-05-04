package com.meclist.validator;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.Checklist;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.enums.StatusItem;
import com.meclist.dto.checklist.precificacao.PrecificarChecklistRequest;
import com.meclist.exception.ChecklistStatusInvalidoException;

@Component
public class ChecklistPrecificacaoValidator {

    public void validarCoberturaItensTrocar(Checklist checklist, PrecificarChecklistRequest request) {
        Set<Long> idsTrocarNoChecklist = checklist.getItensChecklist().stream()
                .filter(item -> item.getStatusItem() == StatusItem.TROCAR)
                .map(ItemChecklist::getId)
                .collect(Collectors.toSet());

        Set<Long> idsRecebidosNoRequest = request.itens().stream()
                .map(item -> item.itemChecklistId())
                .collect(Collectors.toSet());

        Set<Long> faltantes = new HashSet<>(idsTrocarNoChecklist);
        faltantes.removeAll(idsRecebidosNoRequest);

        Set<Long> indevidos = new HashSet<>(idsRecebidosNoRequest);
        indevidos.removeAll(idsTrocarNoChecklist);

        if (!faltantes.isEmpty() || !indevidos.isEmpty()) {
            throw new ChecklistStatusInvalidoException(
                    "Precificação inválida. Itens TROCAR faltantes: " + faltantes
                            + ". Itens indevidos no request: " + indevidos + ".");
        }
    }

    public void validarCompletudeDosItensTrocar(Checklist checklist) {
        List<Long> itensIncompletos = checklist.getItensChecklist().stream()
                .filter(item -> item.getStatusItem() == StatusItem.TROCAR)
                .filter(this::itemSemPrecificacaoCompleta)
                .map(ItemChecklist::getId)
                .toList();

        if (!itensIncompletos.isEmpty()) {
            throw new ChecklistStatusInvalidoException(
                    "Não é possível finalizar precificação. Itens TROCAR incompletos: " + itensIncompletos + ".");
        }
    }

    private boolean itemSemPrecificacaoCompleta(ItemChecklist item) {
        if (item.getMaoDeObra() == null || item.getMaoDeObra().compareTo(BigDecimal.ZERO) < 0) {
            return true;
        }

        if (item.getProdutosOrcados() == null || item.getProdutosOrcados().isEmpty()) {
            return true;
        }

        return item.getProdutosOrcados().stream()
                .anyMatch(produto -> produto.getValorUnitario() == null
                        || produto.getValorUnitario().compareTo(BigDecimal.ZERO) < 0);
    }
}