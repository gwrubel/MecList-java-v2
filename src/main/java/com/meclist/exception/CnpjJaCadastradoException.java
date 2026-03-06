package com.meclist.exception;


public class CnpjJaCadastradoException extends DuplicidadeException {

    public CnpjJaCadastradoException(String message) {
        super("CNPJ_JA_CADASTRADO", message);
    }
}
