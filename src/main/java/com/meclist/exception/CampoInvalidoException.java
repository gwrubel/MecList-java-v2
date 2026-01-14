package com.meclist.exception;

public class CampoInvalidoException extends CustomException {
    private final String campo;
    private final String mensagem;

    public CampoInvalidoException(String campo, String mensagem) {
        super(mensagem, 400);
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