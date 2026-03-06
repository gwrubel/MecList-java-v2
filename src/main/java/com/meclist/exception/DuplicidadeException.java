package com.meclist.exception;
import org.springframework.http.HttpStatus;
public abstract class DuplicidadeException extends CustomException {
    public DuplicidadeException(String code, String message) {
        super(HttpStatus.CONFLICT, code, message);
    }
}
