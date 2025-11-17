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

        // Limpar CPF se fornecido
        String cpfLimpo = request.cpf() != null ? ValidatorUsuario.limparCpf(request.cpf()) : null;

        // Validar todos os dados de atualização de forma centralizada
        ValidatorUsuario.validarDadosAtualizacaoMecanico(
            request.nome(),
            request.email(),
            request.senha(),
            request.telefone(),
            cpfLimpo
        );

        var mecanico = mecanicoExistente.get();

        // Atualizar nome
        if (request.nome() != null && !request.nome().trim().isEmpty()) {
            mecanico.atualizarNome(request.nome().trim());
        }

        // Atualizar telefone
        if (request.telefone() != null) {
            mecanico.atualizarTelefone(request.telefone());
        }

        // Atualizar email
        if (request.email() != null) {
            mecanico.atualizarEmail(request.email());
        }

        // Atualizar CPF
        if (cpfLimpo != null) {
            mecanico.atualizarCpf(cpfLimpo);
        }

        // Atualizar situação
        if (request.situacao() != null) {
            mecanico.setSituacao(request.situacao());
        }

        mecanicoGateway.atualizarMecanico(mecanico);
    }

}
