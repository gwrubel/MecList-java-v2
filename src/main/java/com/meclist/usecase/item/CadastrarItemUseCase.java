package com.meclist.usecase.item;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Item;
import com.meclist.domain.enums.CategoriaParteVeiculo;
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
    public Item executar(String nome, CategoriaParteVeiculo parteDoVeiculo, byte[] imagem) {
        // Upload da imagem ilustrativa
        String nomeArquivo = System.currentTimeMillis() + "_" + nome.replaceAll("\\s+", "_") + ".jpg";
        String caminhoRelativo = "itens/" + parteDoVeiculo.name().toLowerCase() + "/" + nomeArquivo;
        
        String urlImagem = uploadImagemGateway.upload(imagem, caminhoRelativo);
        
        // Cria o item com a URL da imagem
        Item item = Item.novo(nome, parteDoVeiculo, urlImagem);
        
        return itemGateway.salvar(item);
    }
}
