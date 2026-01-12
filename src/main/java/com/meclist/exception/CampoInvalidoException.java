package com.meclist.exception;

import java.util.Map;

public class CampoInvalidoException extends CustomException {
    private final Map<String, String> erros;

    public CampoInvalidoException(String campo) {
        super(
            "O campo " + campo + " é inválido.",
            400
        );
        this.erros = Map.of(campo, "inválido");
    }

    public CampoInvalidoException(Map<String, String> erros) {
        super(
            "Erro de validação",
            400
        );
        this.erros = erros;
    }

    public Map<String, String> getErros() {
        return erros;
    }
}
