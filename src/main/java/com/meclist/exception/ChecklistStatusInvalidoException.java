package com.meclist.exception;

import org.springframework.http.HttpStatus;

public class ChecklistStatusInvalidoException extends CustomException {

    public ChecklistStatusInvalidoException(String mensagem) {
        super(HttpStatus.CONFLICT, "CHECKLIST_STATUS_INVALIDO", mensagem);
    }
}
