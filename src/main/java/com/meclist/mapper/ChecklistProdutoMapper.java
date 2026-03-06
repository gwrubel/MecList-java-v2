package com.meclist.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.meclist.domain.ChecklistProduto;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.Produto;
import com.meclist.dto.produto.ProdutoAdicionado;
import com.meclist.persistence.entity.ChecklistProdutoEntity;
import com.meclist.persistence.entity.ItemChecklistEntity;

public class ChecklistProdutoMapper {

    // ============ CRIAÇÃO (a partir do request) ============

    public static ChecklistProduto toDomain(ProdutoAdicionado request,
                                            Produto produto,
                                            ItemChecklist itemChecklist) {
        return ChecklistProduto.novo(
            itemChecklist,
            produto,
            request.quantidade()
        );
    }

    public static List<ChecklistProduto> toDomainList(List<ProdutoAdicionado> requests,
                                                      List<Produto> produtos,
                                                      ItemChecklist itemChecklist) {
        var produtosMap = produtos.stream()
                .collect(Collectors.toMap(Produto::getId, p -> p));

        return requests.stream()
                .map(req -> {
                    Produto produto = produtosMap.get(req.produtoId());
                    return ChecklistProduto.novo(
                        itemChecklist,
                        produto,
                        req.quantidade()
                    );
                })
                .toList();
    }

    // ============ ENTITY ⇄ DOMAIN ============
public static ChecklistProdutoEntity toEntity(ChecklistProduto produto) {
    if (produto == null) return null;

    ChecklistProdutoEntity entity = new ChecklistProdutoEntity();
    entity.setId(produto.getId());

    if (produto.getItemChecklist() != null) {
        ItemChecklistEntity ic = new ItemChecklistEntity();
        ic.setId(produto.getItemChecklist().getId());
        entity.setItemChecklist(ic);
    }

    entity.setProduto(ProdutoMapper.toEntity(produto.getProduto()));
    entity.setQuantidade(produto.getQuantidade());
    entity.setValorUnitario(produto.getValorUnitario());
    entity.setAprovadoCliente(produto.getAprovadoCliente());
    entity.setCriadoEm(produto.getCriadoEm());
    entity.setAtualizadoEm(produto.getAtualizadoEm());
    return entity;
}

    public static ChecklistProduto toDomain(ChecklistProdutoEntity entity) {
        if (entity == null) return null;

        Produto produto = ProdutoMapper.toDomain(entity.getProduto());

        return new ChecklistProduto(
            entity.getId(),
            null, // ItemChecklist será ligado depois (ic.adicionarProdutoOrcado)
            produto,
            entity.getQuantidade(),
            entity.getValorUnitario(),
            entity.getMarca(),
            entity.getAprovadoCliente(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm()
        );
    }
}