package com.meclist.exception;

import org.springframework.http.HttpStatus;

public class QuilometragemMenorQueAtual extends CustomException {
    public QuilometragemMenorQueAtual(String message) {
        super(HttpStatus.BAD_REQUEST, "QUILOMETRAGEM_MENOR_QUE_ATUAL", message);
    }
}
