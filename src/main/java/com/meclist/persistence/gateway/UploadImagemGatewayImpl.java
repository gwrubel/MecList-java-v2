package com.meclist.persistence.gateway;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.meclist.interfaces.UploadImagemGateway;

@Component
public class UploadImagemGatewayImpl implements UploadImagemGateway {

    @Value("${upload.base.dir:C:/uploads}")
    private String baseDir;

    @Override
    public String upload(byte[] imagem, String caminhoRelativoComNome) {
        try {
            Path destino = Paths.get(baseDir).resolve(caminhoRelativoComNome).normalize();
            Files.createDirectories(destino.getParent());
            Files.write(destino, imagem);
            return "/uploads/" + caminhoRelativoComNome.replace('\\', '/');
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage(), e);
        }
    }
}



