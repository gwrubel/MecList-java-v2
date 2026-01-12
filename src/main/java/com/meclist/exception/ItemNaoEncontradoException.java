package com.meclist.exception;

public class ItemNaoEncontradoException extends RuntimeException {
    public ItemNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
    
    public ItemNaoEncontradoException(Long id) {
        super("Item não encontrado com ID: " + id);
    }
}