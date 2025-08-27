
package com.meclist.usecase.mecanico;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.meclist.domain.Mecanico;
import com.meclist.dto.mecanico.MecanicoRequest;
import com.meclist.exception.CampoInvalidoException;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.interfaces.PasswordEncrypter;
import com.meclist.validator.ValidatorUsuario;

@Service
public class CadastrarMecanicoUseCase {

    private final MecanicoGateway mecanicogateway;
    private final PasswordEncrypter encrypter;

    public CadastrarMecanicoUseCase(MecanicoGateway mecanicoGateway, PasswordEncrypter encrypter) {
        this.mecanicogateway = mecanicoGateway;
        this.encrypter = encrypter;
    }

    public void cadastrarMecanico(MecanicoRequest request) {

        ValidatorUsuario.validarCpf(request.cpf());
        ValidatorUsuario.isTelefoneValido(request.telefone());
        ValidatorUsuario.validarEmail(request.email());
        ValidatorUsuario.validarSenha(request.senha());

        if (mecanicogateway.buscarPorEmail(request.email()).isPresent()) {
            throw new CampoInvalidoException(Map.of("email", "E-mail já cadastrado!"));
        }
        if (mecanicogateway.buscarPorCpf(request.cpf()).isPresent()) {
            throw new CampoInvalidoException(Map.of("cpf", "CPF já cadastrado!"));
        }

        String senhaHash = encrypter.hash(request.senha());

        Mecanico novoMecanico = Mecanico.novoCadastro(
                request.cpf(),
                request.telefone(),
                request.nome(),
                request.email(),
                senhaHash);

        mecanicogateway.salvar(novoMecanico);
    }
}