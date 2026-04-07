package com.meclist.exception;

public class TokenNaoEncontradoException extends NaoEcontradoException {
    public TokenNaoEncontradoException(String message) {
        super("TOKEN_NAO_ENCONTRADO", message);
    }
    
}
