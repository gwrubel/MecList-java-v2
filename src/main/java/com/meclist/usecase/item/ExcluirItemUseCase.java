package com.meclist.usecase.item;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.meclist.interfaces.ItemGateway;
import com.meclist.domain.Item;
import com.meclist.exception.ItemNaoEncontradoException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ExcluirItemUseCase {
    private final ItemGateway itemGateway;
    
    @Value("${upload.path:uploads}")
    private String uploadPath;
    
    public ExcluirItemUseCase(ItemGateway itemGateway) {
        this.itemGateway = itemGateway;
    }
    
    public void executar(Long itemId) {
        // Busca o item para obter o caminho da imagem
        Item item = itemGateway.buscarPorId(itemId)
            .orElseThrow(() -> new ItemNaoEncontradoException(itemId));
        
        // Exclui a imagem física se existir
        if (item.getImagemIlustrativa() != null && !item.getImagemIlustrativa().isEmpty()) {
            excluirImagemFisica(item.getImagemIlustrativa(), item.getParteDoVeiculo().name());
        }
        
        // Exclui o item do banco
        itemGateway.excluir(itemId);
    }
    
    private void excluirImagemFisica(String imagemIlustrativa, String parteDoVeiculo) {
        try {
            // Extrai o nome do arquivo da URL (assume formato: /uploads/filename.jpg)
            String nomeArquivo = imagemIlustrativa.substring(imagemIlustrativa.lastIndexOf("/") + 1);
            String referencia = "itens"+ "/" + parteDoVeiculo.toLowerCase();

            Path caminhoArquivo = Paths.get(uploadPath, referencia, nomeArquivo);
            
            if (Files.exists(caminhoArquivo)) {
                Files.delete(caminhoArquivo);
                System.out.println("Imagem deletada com sucesso: " + caminhoArquivo);
            } else {
                System.out.println("Arquivo de imagem não encontrado: " + caminhoArquivo);
            }
        } catch (Exception e) {
            // Log do erro, mas não impede a exclusão do item
            System.err.println("Erro ao excluir imagem física: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
