package com.meclist.exception;

public class VeiculoJaCadastrado extends DuplicidadeException {
    public VeiculoJaCadastrado(String message) {
        super("VEICULO_JA_CADASTRADO", message);
    }
}
