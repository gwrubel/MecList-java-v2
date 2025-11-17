package com.meclist.usecase.cliente;

import org.springframework.stereotype.Service;

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

        var cliente = clienteExistente.get();

        // Validar e atualizar nome
        if (request.nome() != null && !request.nome().trim().isEmpty()) {
            cliente.atualizarNome(request.nome().trim());
        }

        // Validar e atualizar telefone
        if (request.telefone() != null) {
            ValidatorUsuario.validarTelefone(request.telefone());
            cliente.atualizarTelefone(request.telefone());
        }

        // Validar e atualizar email
        if (request.email() != null) {
            ValidatorUsuario.validarEmail(request.email());
            cliente.atualizarEmail(request.email());
        }

        // Validar e atualizar documento
        if (request.documento() != null && request.tipoDocumento() != null) {
            String documentoLimpo = request.documentoLimpo();
            ValidatorUsuario.validarDocumentoPorTipo(documentoLimpo, request.tipoDocumento());
            cliente.atualizarDocumento(documentoLimpo, request.tipoDocumento());
        }

        // Atualizar situação
        if (request.situacao() != null) {
            cliente.setSituacao(request.situacao());
        }

        // Validar e atualizar endereço
        if (request.endereco() != null && !request.endereco().trim().isEmpty()) {
            cliente.atualizarEndereco(request.endereco().trim());
        }

        clienteGateway.atualizarCliente(cliente);
    }
    

}
