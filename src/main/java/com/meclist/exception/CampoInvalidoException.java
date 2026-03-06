package com.meclist.exception;

import org.springframework.http.HttpStatus;

public class CampoInvalidoException extends CustomException {
    private final String campo;
    private final String mensagem;

    public CampoInvalidoException(String campo, String mensagem) {
        super(HttpStatus.BAD_REQUEST, "CAMPO_INVALIDO", mensagem);
        this.campo = campo;
        this.mensagem = mensagem;
    }

    public String getCampo() {
        return campo;
    }

    public String getMensagem() {
        return mensagem;
    }
}