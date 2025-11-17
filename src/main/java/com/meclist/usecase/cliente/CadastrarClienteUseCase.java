package com.meclist.usecase.cliente;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.meclist.domain.Cliente;
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
        // Limpar documento removendo caracteres não numéricos
        String documentoLimpo = request.documentoLimpo();
        
        // Validar todos os dados do cliente de forma centralizada
        ValidatorUsuario.validarDadosCompletosCliente(
            request.nome(),
            request.email(),
            request.senha(),
            request.telefone(),
            documentoLimpo,
            request.tipoDocumento(),
            request.endereco()
        );

        // Verificar se o cliente já existe
        if (clienteGateway.buscarPorEmail(request.email()).isPresent()) {
            throw new CampoInvalidoException(Map.of("email", "E-mail já cadastrado!"));
        }
        if (clienteGateway.buscarPorDocumento(documentoLimpo).isPresent()) {
            throw new CampoInvalidoException(Map.of("documento", "Documento já cadastrado!"));
        }

        // Criptografar a senha
        String senhaHash = encrypter.hash(request.senha());

        Cliente novoCliente = Cliente.novoCadastro(documentoLimpo,
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
