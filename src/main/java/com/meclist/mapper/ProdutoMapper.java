package com.meclist.mapper;

import com.meclist.domain.Produto;
import com.meclist.domain.ItemChecklist;
import com.meclist.persistence.entity.ItemChecklistEntity;
import com.meclist.persistence.entity.ProdutoEntity;
import java.math.BigDecimal;

public class ProdutoMapper {

    public static ProdutoEntity toEntity(Produto produto, ItemChecklistEntity itemChecklist) {
        if (produto == null) {
            return null;
        }

        ProdutoEntity entity = new ProdutoEntity();
        entity.setId(produto.getId());
        entity.setItemChecklist(itemChecklist);
        entity.setNomeProduto(produto.getNomeProduto());
        entity.setQuantidade(produto.getQuantidade());
        // Converter BigDecimal para Float, tratando valor nulo
        if (produto.getValorUnitario() != null) {
            entity.setValorUnitario(produto.getValorUnitario().floatValue());
        } else {
            entity.setValorUnitario(0.0f); // Valor padrão quando não definido
        }
        entity.setCriadoEm(produto.getCriadoEm());
        entity.setAtualizadoEm(produto.getAtualizadoEm());
        
        return entity;
    }

    public static Produto toDomain(ProdutoEntity entity) {
        if (entity == null) {
            return null;
        }

        // Criar um ItemChecklist temporário usando o construtor com IDs (para compatibilidade)
        ItemChecklist itemChecklist = new ItemChecklist(
            entity.getItemChecklist().getId(), 
            entity.getItemChecklist().getId(), // idChecklist
            null, // idItem
            null, // idStatusItem
            entity.getCriadoEm(), 
            entity.getAtualizadoEm()
        );

        // Converter Float para BigDecimal, tratando valor 0.0 como nulo
        BigDecimal valorUnitario = null;
        if (entity.getValorUnitario() != null && entity.getValorUnitario() > 0.0f) {
            valorUnitario = BigDecimal.valueOf(entity.getValorUnitario());
        }

        return new Produto(
                entity.getId(),
                itemChecklist,
                entity.getNomeProduto(),
                entity.getQuantidade(),
                valorUnitario,
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
}