package com.meclist.usecase.item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.meclist.domain.Item;
import com.meclist.dto.item.AtualizarItemRequest;
import com.meclist.dto.item.ItemResponse;
import com.meclist.exception.ItemNaoEncontradoException;
import com.meclist.interfaces.ItemGateway;
import com.meclist.interfaces.UploadImagemGateway;
import com.meclist.mapper.ItemMapper;

@Service
public class AtualizarItemUseCase {

    private final ItemGateway itemGateway;
    private final UploadImagemGateway uploadImagemGateway;

    @Value("${upload.path:uploads}")
    private String uploadPath;

    public AtualizarItemUseCase(ItemGateway itemGateway, UploadImagemGateway uploadImagemGateway) {
        this.itemGateway = itemGateway;
        this.uploadImagemGateway = uploadImagemGateway;
    }

    @Transactional
    public ItemResponse executar(Long id, AtualizarItemRequest request, MultipartFile novaImagem) {
        Optional<Item> optItem = itemGateway.buscarPorId(id);
        if (optItem.isEmpty()) {
            throw new ItemNaoEncontradoException("Item não encontrado para id: " + id);
        }
        Item item = optItem.get();

        String imagemAtual = item.getImagemIlustrativa();

        // Atualiza imagem se fornecida
        if (novaImagem != null && !novaImagem.isEmpty()) {
            try {
                excluirImagemFisica(imagemAtual, item.getParteDoVeiculo().name());

                String nomeArquivo = System.currentTimeMillis() + "_"
                        + request.nome().replaceAll("\\s+", "_") + ".jpg";
                String caminhoRelativo = "itens/"
                        + request.parteDoVeiculo().name().toLowerCase()
                        + "/" + nomeArquivo;
                byte[] imagemBytes = novaImagem.getBytes();
                String urlImagem = uploadImagemGateway.upload(imagemBytes, caminhoRelativo);

                imagemAtual = urlImagem;
            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar imagem: " + e.getMessage(), e);
            }
        }

        // Usa o método atualizarItem que já atualiza a data automaticamente
        item.atualizarItem(request.nome(), request.parteDoVeiculo(), imagemAtual);

        Item itemAtualizado = itemGateway.salvar(item);
        return ItemMapper.toResponse(itemAtualizado);
    }

    private void excluirImagemFisica(String imagemIlustrativa, String parteDoVeiculo) {
        try {
            String nomeArquivo = imagemIlustrativa.substring(imagemIlustrativa.lastIndexOf("/") + 1);
            String referencia = "itens" + "/" + parteDoVeiculo.toLowerCase();
            Path caminhoArquivo = Paths.get(uploadPath, referencia, nomeArquivo);

            if (Files.exists(caminhoArquivo)) {
                Files.delete(caminhoArquivo);
                System.out.println("Imagem deletada com sucesso: " + caminhoArquivo);
            } else {
                System.out.println("Arquivo de imagem não encontrado: " + caminhoArquivo);
            }
        } catch (Exception e) {
            System.err.println("Erro ao excluir imagem física: " + e.getMessage());
            e.printStackTrace();
        }
    }
}