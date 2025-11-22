package com.meclist.usecase.cliente;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.meclist.domain.enums.TipoDocumento;
import com.meclist.dto.cliente.AtualizarClienteRequest;
import com.meclist.exception.CampoInvalidoException;
import com.meclist.exception.EmailJaCadastrado;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.validator.ValidatorUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AtualizarDadosClienteUseCase {

    private final ClienteGateway clienteGateway;

    public AtualizarDadosClienteUseCase(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }

    public void atualizarDados(AtualizarClienteRequest request, Long id) {
        var cliente = clienteGateway.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado!"));

        if (request.nome() != null) {
            cliente.atualizarNome(request.nome());
        }

        if (request.telefone() != null) {
            ValidatorUtils.validarTelefone(request.telefone());
            cliente.atualizarTelefone(request.telefone());
        }

        if (request.email() != null) {
            ValidatorUtils.validarEmail(request.email());
            
            if (!cliente.getEmail().equals(request.email())) {
                var clienteComMesmoEmail = clienteGateway.buscarPorEmail(request.email());
                if (clienteComMesmoEmail.isPresent() && !clienteComMesmoEmail.get().getId().equals(id)) {
                    throw new EmailJaCadastrado("O e-mail '" + request.email() + "' já está cadastrado para outro cliente");
                }
            }
            
            cliente.atualizarEmail(request.email());
        }

        if (request.documento() != null && request.tipoDocumento() != null) {
            if (request.tipoDocumento() == TipoDocumento.CPF) {
                ValidatorUtils.validarCpf(request.documento());
            } else if (request.tipoDocumento() == TipoDocumento.CNPJ) {
                ValidatorUtils.validarCnpj(request.documento());
            }
            
            if (!cliente.getDocumento().equals(request.documento())) {
                var clienteComMesmoDocumento = clienteGateway.buscarPorDocumento(request.documento());
                if (clienteComMesmoDocumento.isPresent() && !clienteComMesmoDocumento.get().getId().equals(id)) {
                    throw new CampoInvalidoException(Map.of("documento", "Documento já cadastrado!"));
                }
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