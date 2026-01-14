package com.meclist.exception;

public class CpfJaCadastrado extends CustomException {
    public CpfJaCadastrado(String cpf) {
        super(
            "CPF já está cadastrado.",
            409
        );
    }
}

