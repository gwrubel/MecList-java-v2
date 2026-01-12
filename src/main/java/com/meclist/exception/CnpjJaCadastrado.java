package com.meclist.exception;

public class CnpjJaCadastrado extends CustomException {

    public CnpjJaCadastrado(String cnpj) {
        super(
            "O CNPJ " + cnpj + " já está cadastrado.",
            409
        );
    }
}
