package com.meclist.exception;

import org.springframework.http.HttpStatus;

public abstract class NaoEcontradoException extends CustomException {
    public NaoEcontradoException(String code, String message) {
        super(HttpStatus.NOT_FOUND, code, message);
    }
}
