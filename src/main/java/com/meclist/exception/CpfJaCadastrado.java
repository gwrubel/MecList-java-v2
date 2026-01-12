package com.meclist.exception;

public class CpfJaCadastrado extends CustomException {
    public CpfJaCadastrado(String cpf) {
        super(
            "O CPF: " + cpf + " já está cadastrado.",
            409
        );
    }
}

