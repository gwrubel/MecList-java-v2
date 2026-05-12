package com.meclist.exception;

public class MecanicoNaoEncontradoException extends NaoEcontradoException {
    public MecanicoNaoEncontradoException(String mensagem) {
        super("MECANICO_NAO_ENCONTRADO", mensagem);
    }
    
    public MecanicoNaoEncontradoException(Long id) {
        super("MECANICO_NAO_ENCONTRADO", "Mecânico não encontrado com ID: " + id);
    }
    
}
