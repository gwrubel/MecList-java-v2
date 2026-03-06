package com.meclist.exception;

public class EmailJaCadastrado extends DuplicidadeException {
    public EmailJaCadastrado(String message) {
        super("EMAIL_JA_CADASTRADO", message);
    }
}