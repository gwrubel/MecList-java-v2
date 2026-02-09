package com.meclist.exception;

public class VeiculoNaoEncontrado extends RuntimeException {
    public VeiculoNaoEncontrado(String mensagem) {
        super(mensagem);
    }
    
    public VeiculoNaoEncontrado(Long id) {
        super("Veículo não encontrado com ID: " + id);
    }
    
}
