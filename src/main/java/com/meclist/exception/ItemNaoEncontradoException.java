package com.meclist.exception;

import org.springframework.http.HttpStatus;

public class ItemNaoEncontradoException extends CustomException {

    public ItemNaoEncontradoException(String mensagem) {
        super(HttpStatus.NOT_FOUND, "ITEM_NAO_ENCONTRADO", mensagem);
    }
    
    
}