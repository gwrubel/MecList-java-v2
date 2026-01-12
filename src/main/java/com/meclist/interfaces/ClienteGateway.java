package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;

import com.meclist.domain.Cliente;
import com.meclist.domain.enums.Situacao;

public interface ClienteGateway {
    public Cliente salvar(Cliente cliente);

    public Optional<Cliente> buscarPorEmail(String email);

    public Optional<Cliente> buscarPorDocumento(String documento);

    public Optional<Cliente> buscarPorId(Long id);

    List<Cliente> buscarTodos();

    public Cliente atualizarCliente(Cliente cliente);

    List<Cliente> buscarPorSituacao(Situacao situacao);
}
