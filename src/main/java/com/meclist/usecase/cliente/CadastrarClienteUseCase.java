package com.meclist.usecase.cliente;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.meclist.domain.Cliente;
import com.meclist.domain.enums.TipoDocumento;
import com.meclist.dto.cliente.ClienteRequest;
import com.meclist.exception.CampoInvalidoException;
import com.meclist.interfaces.ClienteGateway;
import com.meclist.interfaces.PasswordEncrypter;
import com.meclist.validator.ValidatorUsuario;

@Service
public class CadastrarClienteUseCase {
    private final ClienteGateway clienteGateway;
    private final PasswordEncrypter encrypter;

    public CadastrarClienteUseCase(ClienteGateway clienteGateway, PasswordEncrypter encrypter) {
        this.clienteGateway = clienteGateway;
        this.encrypter = encrypter;
    }

    public void cadastrarCliente(ClienteRequest request) {
        // Validar telefone e email
        ValidatorUsuario.validarTelefone(request.telefone());
        ValidatorUsuario.validarEmail(request.email());
        ValidatorUsuario.validarSenha(request.senha());

        // Validar documento baseado no tipo
        if (request.tipoDocumento() == TipoDocumento.CPF) {
            ValidatorUsuario.validarCpf(request.documento());
        } else if (request.tipoDocumento() == TipoDocumento.CNPJ) {
            ValidatorUsuario.validarCnpj(request.documento());
        }

        // Verificar se o cliente já existe
        if (clienteGateway.buscarPorEmail(request.email()).isPresent()) {
            throw new CampoInvalidoException(Map.of("email", "E-mail já cadastrado!"));
        }
        if (clienteGateway.buscarPorDocumento(request.documento()).isPresent()) {
            throw new CampoInvalidoException(Map.of("documento", "Documento já cadastrado!"));
        }

        // Criptografar a senha
        String senhaHash = encrypter.hash(request.senha());

        
        Cliente novoCliente = Cliente.novoCadastro(request.documento(),
         request.tipoDocumento(),
         request.telefone(), 
         request.nome(), 
         request.email(),
         senhaHash, 
         request.endereco()
        );

        clienteGateway.salvar(novoCliente);
    }
}
