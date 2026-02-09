package com.meclist.exception;

public class MecanicoNaoEncontradoException extends RuntimeException {
    public MecanicoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
    
    public MecanicoNaoEncontradoException(Long id) {
        super("Mecânico não encontrado com ID: " + id);
    }
    
}
