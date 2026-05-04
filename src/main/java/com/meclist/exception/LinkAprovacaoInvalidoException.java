package com.meclist.exception;

import org.springframework.http.HttpStatus;

public class LinkAprovacaoInvalidoException extends CustomException {

    public LinkAprovacaoInvalidoException(String mensagem) {
        super(HttpStatus.UNAUTHORIZED, "LINK_APROVACAO_INVALIDO", mensagem);
    }
}