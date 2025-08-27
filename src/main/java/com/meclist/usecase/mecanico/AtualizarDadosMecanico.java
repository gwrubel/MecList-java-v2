package com.meclist.usecase.mecanico;

import org.springframework.stereotype.Service;

import com.meclist.dto.mecanico.AtualizarMecanicoRequest;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.validator.ValidatorUsuario;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AtualizarDadosMecanico {

    private final MecanicoGateway mecanicoGateway;

    public AtualizarDadosMecanico(MecanicoGateway mecanicoGateway) {
        this.mecanicoGateway = mecanicoGateway;

    }

    public void atualizarDados(AtualizarMecanicoRequest request, Long id) {

        var mecanicoExistente = mecanicoGateway.bucarPorId(id);

        if (mecanicoExistente.isEmpty()) {
            throw new EntityNotFoundException("Mecânico com ID " + id + " não encontrado!");
        }

        var mecanico = mecanicoExistente.get();

        if (request.telefone() != null) {
            ValidatorUsuario.isTelefoneValido(request.telefone());
            mecanico.atualizarTelefone(request.telefone());
        }

        if (request.email() != null) {
            ValidatorUsuario.validarEmail(request.email());
            mecanico.atualizarEmail(request.email());
        }

        if (request.nome() != null) {
            mecanico.atualizarNome(request.nome());
        }

        if (request.telefone() != null) {
            mecanico.atualizarTelefone(request.telefone());
        }

        if (request.email() != null) {
            mecanico.atualizarEmail(request.email());
        }

        if (request.cpf() != null) {
            mecanico.atualizarCpf(request.cpf());
        }

        if (request.situacao() != null) {
            mecanico.setSituacao(request.situacao());
        }

        mecanicoGateway.atualizarMecanico(mecanico);

    }

}
