package com.meclist.usecase.item;

import org.springframework.stereotype.Service;


import com.meclist.interfaces.ItemGateway;
import com.meclist.domain.Item;
import com.meclist.domain.enums.Situacao;
import com.meclist.exception.ItemNaoEncontradoException;

@Service
public class AlterarSituacaoDoItemUseCase {
    private final ItemGateway itemGateway;
   
    
    public AlterarSituacaoDoItemUseCase(ItemGateway itemGateway) {
        this.itemGateway = itemGateway;
    }
    
    public void ativar(Long id) {
         executar(id, Situacao.ATIVO);
    }


    public void desativar(Long id) {
         executar(id, Situacao.INATIVO);
    }

    public void executar(Long itemId, Situacao situacao) {
        Item item = itemGateway.buscarPorId(itemId)
            .orElseThrow(() -> new ItemNaoEncontradoException(
                "Item com ID " + itemId + " nao encontrado"
            ));

            System.out.println("Alterando situacao do item ID " + itemId + " para " + situacao);

        if (situacao == Situacao.ATIVO) item.ativar();
        else if (situacao == Situacao.INATIVO) item.desativar();
        else throw new IllegalArgumentException("Situacao invalida: " + situacao);
        System.out.println("Situacao atual do item ID " + itemId + ": " + item.getSituacao());
         itemGateway.salvar(item);
    }
}
