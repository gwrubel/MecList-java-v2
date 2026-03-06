package com.meclist.exception;

public class ProdutoJaExisteException extends DuplicidadeException {
    public ProdutoJaExisteException(String message) {
        super("PRODUTO_JA_EXISTE", message);
    }
}
