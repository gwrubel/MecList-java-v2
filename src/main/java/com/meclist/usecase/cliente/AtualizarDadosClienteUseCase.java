package com.meclist.usecase.cliente;

import org.springframework.stereotype.Service;

import com.meclist.domain.enums.TipoDocumento;
import com.meclist.dto.cliente.AtualizarClienteRequest;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.validator.ValidatorUsuario;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AtualizarDadosClienteUseCase {

    private final ClienteGateway clienteGateway;

    public AtualizarDadosClienteUseCase(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }

    public void atualizarDados (AtualizarClienteRequest request, Long id) {
        var clienteExistente = clienteGateway.buscarPorId(id);

        if (clienteExistente.isEmpty()) {
            throw new EntityNotFoundException("Cliente com ID " + id + " não encontrado!");
        }

        ValidatorUsuario.validarTelefone(request.telefone());
        ValidatorUsuario.validarEmail(request.email());

        var cliente = clienteExistente.get();

        if (request.nome() != null) {
            cliente.atualizarNome(request.nome());
        }

        if (request.telefone() != null) {
            cliente.atualizarTelefone(request.telefone());
        }

        if (request.email() != null) {
            cliente.atualizarEmail(request.email());
        }

        if (request.documento() != null && request.tipoDocumento() != null) {
            // Validar documento baseado no tipo
            if (request.tipoDocumento() == TipoDocumento.CPF) {
                ValidatorUsuario.validarCpf(request.documento());
            } else if (request.tipoDocumento() == TipoDocumento.CNPJ) {
                ValidatorUsuario.validarCnpj(request.documento());
            }
            cliente.atualizarDocumento(request.documento(), request.tipoDocumento());
        }

        if (request.situacao() != null) {
            cliente.setSituacao(request.situacao());
        }

        if (request.endereco() != null) {
            cliente.atualizarEndereco(request.endereco());
        }

        clienteGateway.atualizarCliente(cliente);
    }
    

}
