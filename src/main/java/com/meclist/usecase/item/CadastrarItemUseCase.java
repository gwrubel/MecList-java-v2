package com.meclist.usecase.item;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.meclist.domain.Item;
import com.meclist.dto.item.CadastrarItemRequest;
import com.meclist.dto.item.ItemResponse;
import com.meclist.exception.ItemDuplicado;
import com.meclist.interfaces.ItemGateway;
import com.meclist.interfaces.UploadImagemGateway;
import com.meclist.mapper.ItemMapper;

@Service
public class CadastrarItemUseCase {

    private final ItemGateway itemGateway;
    private final UploadImagemGateway uploadImagemGateway;
  

    public CadastrarItemUseCase(
            ItemGateway itemGateway, 
            UploadImagemGateway uploadImagemGateway
            ) {
        this.itemGateway = itemGateway;
        this.uploadImagemGateway = uploadImagemGateway;
    }

    @Transactional
    public ItemResponse executar(CadastrarItemRequest request, MultipartFile imagem, boolean force) {
        try {
            // Validações de imagem
            if (imagem.isEmpty()) {
                throw new IllegalArgumentException("Arquivo de imagem está vazio");
            }
            
            String contentType = imagem.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Arquivo deve ser uma imagem válida");
            }

            // Verifica se já existe item com mesmo nome e categoria
            boolean itemExiste = itemGateway.existeComMesmoNome(
                request.nome()
                
            );
            
            if (itemExiste && !force) {
                throw new ItemDuplicado(
                    "Já existe um item com o nome '" + request.nome() + 
                    "' de categoria " + request.parteDoVeiculo() + 
                    ". Use o parâmetro 'force=true' para cadastrar mesmo assim."
                );
            }

            // Upload da imagem ilustrativa
            String nomeArquivo = System.currentTimeMillis() + "_" 
                               + request.nome().replaceAll("\\s+", "_") + ".jpg";
            String caminhoRelativo = "itens/" 
                                   + request.parteDoVeiculo().name().toLowerCase() 
                                   + "/" + nomeArquivo;
            
            byte[] imagemBytes = imagem.getBytes();
            String urlImagem = uploadImagemGateway.upload(imagemBytes, caminhoRelativo);
            
            // Cria o item com a URL da imagem
            Item item = Item.novo(request.nome(), request.parteDoVeiculo(), urlImagem);
            Item itemSalvo = itemGateway.salvar(item);
            
            return ItemMapper.toResponse(itemSalvo);
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar imagem: " + e.getMessage(), e);
        }
    }
}
