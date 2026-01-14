package com.meclist.exception;

public class CnpjJaCadastrado extends CustomException {

    public CnpjJaCadastrado(String cnpj) {
        super(
            "CNPJ já está cadastrado.",
            409
        );
    }
}
