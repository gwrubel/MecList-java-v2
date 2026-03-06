package com.meclist.exception;

public class ProdutoNaoEncontradoException extends NaoEcontradoException {
    public ProdutoNaoEncontradoException(String message) {
        super("PRODUTO_NAO_ENCONTRADO", message);
    }
    
}
