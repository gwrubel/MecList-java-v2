package com.meclist.usecase.mecanico;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.meclist.dto.mecanico.AtualizarMecanicoRequest;
import com.meclist.exception.CampoInvalidoException;
import com.meclist.exception.EmailJaCadastrado;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.validator.ValidatorUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AtualizarDadosMecanico {

    private final MecanicoGateway mecanicoGateway;

    public AtualizarDadosMecanico(MecanicoGateway mecanicoGateway) {
        this.mecanicoGateway = mecanicoGateway;
    }

    public void atualizarDados(AtualizarMecanicoRequest request, Long id) {

        var mecanico = mecanicoGateway.bucarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Mecânico com ID " + id + " não encontrado!"));

        if (request.nome() != null) {
            mecanico.atualizarNome(request.nome());
        }

        if (request.telefone() != null) {
            ValidatorUtils.validarTelefone(request.telefone());
            mecanico.atualizarTelefone(request.telefone());
        }

        if (request.email() != null) {
            ValidatorUtils.validarEmail(request.email());
            
            if (!mecanico.getEmail().equals(request.email())) {
                var mecanicoComMesmoEmail = mecanicoGateway.buscarPorEmail(request.email());
                if (mecanicoComMesmoEmail.isPresent() && !mecanicoComMesmoEmail.get().getId().equals(id)) {
                    throw new EmailJaCadastrado("O e-mail '" + request.email() + "' já está cadastrado para outro mecânico");
                }
            }
            
            mecanico.atualizarEmail(request.email());
        }

        if (request.cpf() != null) {
            ValidatorUtils.validarCpf(request.cpf());
            
            if (!mecanico.getCpf().equals(request.cpf())) {
                var mecanicoComMesmoCpf = mecanicoGateway.buscarPorCpf(request.cpf());
                if (mecanicoComMesmoCpf.isPresent() && !mecanicoComMesmoCpf.get().getId().equals(id)) {
                    throw new CampoInvalidoException(Map.of("cpf", "CPF já cadastrado!"));
                }
            }
            
            mecanico.atualizarCpf(request.cpf());
        }

        if (request.situacao() != null) {
            mecanico.setSituacao(request.situacao());
        }

        mecanicoGateway.atualizarMecanico(mecanico);
    }
}