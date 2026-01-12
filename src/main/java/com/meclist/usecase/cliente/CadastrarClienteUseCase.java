package com.meclist.usecase.cliente;

import org.springframework.stereotype.Service;

import com.meclist.domain.Cliente;
import com.meclist.domain.enums.TipoDocumento;
import com.meclist.dto.cliente.ClienteRequest;
import com.meclist.dto.cliente.ClienteResponse;
import com.meclist.exception.CnpjJaCadastrado;
import com.meclist.exception.CpfJaCadastrado;
import com.meclist.exception.EmailJaCadastrado;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.interfaces.PasswordEncrypter;
import com.meclist.mapper.ClienteMapper;
import com.meclist.validator.ValidatorUtils;

@Service
public class CadastrarClienteUseCase {
    private final ClienteGateway clienteGateway;
    private final PasswordEncrypter encrypter;

    public CadastrarClienteUseCase(ClienteGateway clienteGateway, PasswordEncrypter encrypter) {
        this.clienteGateway = clienteGateway;
        this.encrypter = encrypter;
    }

    public ClienteResponse cadastrarCliente(ClienteRequest request) {
        ValidatorUtils.validarTelefone(request.telefone());
        ValidatorUtils.validarEmail(request.email());
        ValidatorUtils.validarSenha(request.senha());

        
        String documentoLimpo = request.documento().replaceAll("\\D", "");
    
        if (request.tipoDocumento() == TipoDocumento.CPF) {
            ValidatorUtils.validarCpf(documentoLimpo);
        } else if (request.tipoDocumento() == TipoDocumento.CNPJ) {
            ValidatorUtils.validarCnpj(documentoLimpo);
        }

        if (clienteGateway.buscarPorEmail(request.email()).isPresent()) {
            throw new EmailJaCadastrado("E-mail já cadastrado!");
        }

        if (clienteGateway.buscarPorDocumento(documentoLimpo).isPresent()) {
            if (request.tipoDocumento() == TipoDocumento.CPF) {
                throw new CpfJaCadastrado(documentoLimpo);
            } else {
                throw new CnpjJaCadastrado(documentoLimpo);
            }
        }

        String senhaHash = encrypter.hash(request.senha());

        Cliente novoCliente = Cliente.novoCadastro(
            documentoLimpo,  
            request.tipoDocumento(),
            request.telefone(), 
            request.nome(), 
            request.email(),
            senhaHash, 
            request.endereco()
        );

      Cliente clienteSalvo = clienteGateway.salvar(novoCliente);
      return ClienteMapper.toResponse(clienteSalvo);
    }
}