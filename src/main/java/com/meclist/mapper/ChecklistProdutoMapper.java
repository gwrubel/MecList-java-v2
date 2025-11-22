package com.meclist.mapper;

import com.meclist.domain.ChecklistProduto;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.Produto;
import com.meclist.persistence.entity.ChecklistProdutoEntity;
import com.meclist.persistence.entity.ItemChecklistEntity;
import com.meclist.persistence.entity.ProdutoEntity;

public class ChecklistProdutoMapper {

    public static ChecklistProdutoEntity toEntity(ChecklistProduto checklistProduto, 
                                                   ItemChecklistEntity itemChecklist, 
                                                   ProdutoEntity produto) {
        if (checklistProduto == null) {
            return null;
        }

        ChecklistProdutoEntity entity = new ChecklistProdutoEntity();
        entity.setId(checklistProduto.getId());
        entity.setItemChecklist(itemChecklist);
        entity.setProduto(produto);
        entity.setQuantidade(checklistProduto.getQuantidade());
        
        // Converter BigDecimal para BigDecimal (mantém precisão)
        entity.setValorUnitario(checklistProduto.getValorUnitario());
        
        entity.setAprovadoCliente(checklistProduto.getAprovadoCliente());
        entity.setCriadoEm(checklistProduto.getCriadoEm());
        entity.setAtualizadoEm(checklistProduto.getAtualizadoEm());
        
        return entity;
    }

    public static ChecklistProduto toDomain(ChecklistProdutoEntity entity) {
        if (entity == null) {
            return null;
        }

        // Criar ItemChecklist mínimo usando o construtor com IDs (para compatibilidade)
        ItemChecklist itemChecklist = new ItemChecklist(
            entity.getItemChecklist().getId(),
            entity.getItemChecklist().getChecklist().getId(), // idChecklist
            entity.getItemChecklist().getItem().getId(), // idItem
            entity.getItemChecklist().getStatusItem().getId(), // idStatusItem
            entity.getItemChecklist().getCriadoEm(),
            entity.getItemChecklist().getAtualizadoEm()
        );

        Produto produto = ProdutoMapper.toDomain(entity.getProduto());

        return new ChecklistProduto(
                entity.getId(),
                itemChecklist,
                produto,
                entity.getQuantidade(),
                entity.getValorUnitario(),
                entity.getAprovadoCliente(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
}

