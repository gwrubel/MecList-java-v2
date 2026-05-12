package com.meclist.exception;

public class ServicoNaoEncontradoException extends NaoEcontradoException {

    public ServicoNaoEncontradoException(Long servicoId) {
        super("SERVICO_NAO_ENCONTRADO", "Serviço não encontrado: " + servicoId);
    }
}
