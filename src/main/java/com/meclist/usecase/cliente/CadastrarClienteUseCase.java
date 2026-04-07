package com.meclist.usecase.cliente;

import org.springframework.stereotype.Service;

import com.meclist.domain.Cliente;
import com.meclist.domain.TokenDefinicaoSenha;
import com.meclist.domain.enums.TipoDocumento;
import com.meclist.dto.cliente.ClienteRequest;
import com.meclist.dto.cliente.ClienteResponse;
import com.meclist.exception.CnpjJaCadastradoException;
import com.meclist.exception.CpfJaCadastrado;
import com.meclist.exception.EmailJaCadastrado;
import com.meclist.infra.EmailService;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.interfaces.TokenDefinicaoSenhaGateway;
import com.meclist.mapper.ClienteMapper;
import com.meclist.validator.ValidatorUtils;

@Service
public class CadastrarClienteUseCase {
    private final ClienteGateway clienteGateway;
    private final TokenDefinicaoSenhaGateway tokenGateway;
    private final EmailService emailService;

    public CadastrarClienteUseCase(ClienteGateway clienteGateway,
                                   TokenDefinicaoSenhaGateway tokenGateway,
                                   EmailService emailService) {
        this.clienteGateway = clienteGateway;
        this.tokenGateway = tokenGateway;
        this.emailService = emailService;
    }

    public ClienteResponse cadastrarCliente(ClienteRequest request) {
        ValidatorUtils.validarTelefone(request.telefone());
        ValidatorUtils.validarEmail(request.email());

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
                throw new CpfJaCadastrado("CPF ja cadastrado!");
            } else {
                throw new CnpjJaCadastradoException("CNPJ ja cadastrado!");
            }
        }

        Cliente novoCliente = Cliente.novoCadastro(
            documentoLimpo,  
            request.tipoDocumento(),
            request.telefone(), 
            request.nome(), 
            request.email(),
            request.endereco()
        );

        Cliente clienteSalvo = clienteGateway.salvar(novoCliente);

        // Gera token e envia email para o cliente definir a senha
        TokenDefinicaoSenha token = TokenDefinicaoSenha.gerar(clienteSalvo.getEmail(), 48);
        tokenGateway.salvar(token);
        emailService.enviarEmailDefinicaoSenha(clienteSalvo.getEmail(), token.getToken());

        return ClienteMapper.toResponse(clienteSalvo);
    }
}