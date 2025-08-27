package com.meclist.exception;

public abstract class DuplicidadeException extends RuntimeException {
    public DuplicidadeException(String message) {
        super(message);
    }
}
