package com.meclist.usecase.mecanico;

import org.springframework.stereotype.Service;

import com.meclist.domain.Mecanico;
import com.meclist.dto.mecanico.MecanicoRequest;
import com.meclist.exception.CpfJaCadastrado;
import com.meclist.exception.EmailJaCadastrado;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.interfaces.PasswordEncrypter;
import com.meclist.validator.ValidatorUtils;

@Service
public class CadastrarMecanicoUseCase {

    private final MecanicoGateway mecanicogateway;
    private final PasswordEncrypter encrypter;

    public CadastrarMecanicoUseCase(MecanicoGateway mecanicoGateway, PasswordEncrypter encrypter) {
        this.mecanicogateway = mecanicoGateway;
        this.encrypter = encrypter;
    }

    public void cadastrarMecanico(MecanicoRequest request) {

        ValidatorUtils.validarCpf(request.cpf());
        ValidatorUtils.validarTelefone(request.telefone());
        ValidatorUtils.validarEmail(request.email());
        ValidatorUtils.validarSenha(request.senha());

        if (mecanicogateway.buscarPorEmail(request.email()).isPresent()) {
            throw new EmailJaCadastrado("E-mail já cadastrado!");
        }
        
        if (mecanicogateway.buscarPorCpf(request.cpf()).isPresent()) {
            throw new CpfJaCadastrado("CPF já cadastrado!");
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