package com.meclist.exception;

public class ClienteNaoEncontradoException extends NaoEcontradoException {
    public ClienteNaoEncontradoException(String message) {
        super("CLIENTE_NAO_ENCONTRADO", message);
    }
    
}
