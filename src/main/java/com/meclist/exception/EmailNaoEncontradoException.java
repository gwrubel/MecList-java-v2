package com.meclist.exception;

public class EmailNaoEncontradoException  extends NaoEcontradoException {
    public EmailNaoEncontradoException(String message) {
        super("EMAIL_NAO_ENCONTRADO", message);
    }
    
}
