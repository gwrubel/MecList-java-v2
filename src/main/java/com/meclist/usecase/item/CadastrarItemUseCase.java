package com.meclist.usecase.item;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Item;
import com.meclist.dto.item.CadastrarItemRequest;
import com.meclist.interfaces.ItemGateway;
import com.meclist.interfaces.UploadImagemGateway;

@Service
public class CadastrarItemUseCase {

    private final ItemGateway itemGateway;
    private final UploadImagemGateway uploadImagemGateway;

    public CadastrarItemUseCase(ItemGateway itemGateway, UploadImagemGateway uploadImagemGateway) {
        this.itemGateway = itemGateway;
        this.uploadImagemGateway = uploadImagemGateway;
    }

    @Transactional
    public Item executar(CadastrarItemRequest request, byte[] imagem) {
        // Upload da imagem ilustrativa
        String nomeArquivo = System.currentTimeMillis() + "_" + request.nome().replaceAll("\\s+", "_") + ".jpg";
        String caminhoRelativo = "itens/" + request.parteDoVeiculo().name().toLowerCase() + "/" + nomeArquivo;
        
        String urlImagem = uploadImagemGateway.upload(imagem, caminhoRelativo);
        
        // Cria o item com a URL da imagem
        Item item = Item.novo(request.nome(), request.parteDoVeiculo(), urlImagem);
        
        return itemGateway.salvar(item);
    }
}
