package com.meclist.exception;

import java.util.Map;

public class CampoInvalidoException extends RuntimeException {
    private final Map<String, String> erros;

    public CampoInvalidoException(Map<String, String> erros) {
        super("Campos inválidos.");
        this.erros = erros;
    }

    public Map<String, String> getErros() {
        return erros;
    }
}
