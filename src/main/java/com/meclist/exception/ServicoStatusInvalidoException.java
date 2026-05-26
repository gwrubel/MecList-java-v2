package com.meclist.exception;

import org.springframework.http.HttpStatus;

public class ServicoStatusInvalidoException extends CustomException {

    public ServicoStatusInvalidoException(String mensagem) {
        super(HttpStatus.CONFLICT, "SERVICO_STATUS_INVALIDO", mensagem);
    }
}
