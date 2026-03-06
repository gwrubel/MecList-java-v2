package com.meclist.exception;

public class ChecklistNaoEncontradoException extends NaoEcontradoException {
    public ChecklistNaoEncontradoException(String message) {
        super("CHECKLIST_NAO_ENCONTRADO", message);
    }
}
