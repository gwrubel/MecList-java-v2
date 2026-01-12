package com.meclist.exception;

public abstract class DuplicidadeException extends CustomException {
    public DuplicidadeException(String message) {
        super(message, 409);
    }
}
