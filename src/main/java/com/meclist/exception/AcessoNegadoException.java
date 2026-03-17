package com.meclist.exception;

import org.springframework.http.HttpStatus;

public class AcessoNegadoException extends CustomException {

    public AcessoNegadoException(String mensagem) {
        super(HttpStatus.FORBIDDEN, "ACESSO_NEGADO", mensagem);
    }
}
