package com.meclist.usecase.item;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.meclist.domain.Item;
import com.meclist.domain.enums.Situacao;
import com.meclist.dto.item.AtualizarItemRequest;
import com.meclist.dto.item.ItemResponse;
import com.meclist.exception.ItemNaoEncontradoException;
import com.meclist.interfaces.ItemGateway;
import com.meclist.interfaces.ItemProdutoGateway;
import com.meclist.interfaces.UploadImagemGateway;
import com.meclist.mapper.ItemMapper;

@Service
public class AtualizarItemUseCase {

    private final ItemGateway itemGateway;
    private final ItemProdutoGateway itemProdutoGateway;
    private final UploadImagemGateway uploadImagemGateway;

    public AtualizarItemUseCase(ItemGateway itemGateway, ItemProdutoGateway itemProdutoGateway, UploadImagemGateway uploadImagemGateway) {
        this.itemGateway = itemGateway;
        this.itemProdutoGateway = itemProdutoGateway;
        this.uploadImagemGateway = uploadImagemGateway;
    }

    @Transactional
    public ItemResponse executar(Long id, AtualizarItemRequest request, MultipartFile novaImagem) {
        Optional<Item> optItem = itemGateway.buscarPorId(id);
        if (optItem.isEmpty()) {
            throw new ItemNaoEncontradoException("Item não encontrado para id: " + id);
        }

        boolean itemDuplicado = itemGateway.existeComMesmoNomeECategoria(
                request.nome(),
                request.parteDoVeiculo(),
                id);

        if (itemDuplicado) {
            throw new IllegalArgumentException("Já existe um item com o nome '" + request.nome() + "' nessa categoria.");
        }

        Item item = optItem.get();

        String imagemAtual = item.getImagemIlustrativa();

        // Atualiza imagem se fornecida
        if (novaImagem != null && !novaImagem.isEmpty()) {
            try {
                uploadImagemGateway.delete(imagemAtual);

                String nomeArquivo = System.currentTimeMillis() + "_"
                        + request.nome().replaceAll("\\s+", "_") + ".jpg";
                String caminhoRelativo = "itens/"
                        + request.parteDoVeiculo().name().toLowerCase()
                        + "/" + nomeArquivo;
                byte[] imagemBytes = novaImagem.getBytes();
                String contentType = novaImagem.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new IllegalArgumentException("Arquivo deve ser uma imagem válida");
                }

                String urlImagem = uploadImagemGateway.upload(imagemBytes, caminhoRelativo, contentType);

                imagemAtual = urlImagem;
            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar imagem: " + e.getMessage(), e);
            }
        }

        // Usa o método atualizarItem que já atualiza a data automaticamente
        item.atualizarItem(request.nome(), request.parteDoVeiculo(), imagemAtual);

        Item itemAtualizado = itemGateway.salvar(item);
        return ItemMapper.toResponse(itemAtualizado, (int) itemProdutoGateway.contarPorItem(itemAtualizado.getId()));
    }
}