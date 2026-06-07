package com.meclist.interfaces;

public interface UploadImagemGateway {
    String upload(byte[] imagem, String caminhoRelativoComNome, String contentType);

    void delete(String imagemReferencia);
}



