package com.meclist.exception;

public class CpfJaCadastrado extends DuplicidadeException {

    public CpfJaCadastrado(String message) {
        super("CPF_JA_CADASTRADO", message);
    }
}

