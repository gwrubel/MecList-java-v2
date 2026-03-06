package com.meclist.exception;

public class VeiculoNaoEncontrado extends NaoEcontradoException {
    public VeiculoNaoEncontrado(String message) {
        super("VEICULO_NAO_ENCONTRADO", message);
    }
  
    
}
