package com.meclist.infra;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.meclist.interfaces.NotificacaoGateway;

@Service
public class NotificacaoGatewayEmailImpl implements NotificacaoGateway {

    private final EmailService emailService;

    public NotificacaoGatewayEmailImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void notificarVeiculoProntoRetirada(Long servicoId,
                                               Long checklistId,
                                               String emailCliente,
                                               String nomeCliente,
                                               String placa,
                                               String marca,
                                               String modelo,
                                               LocalDateTime concluidoEm) {
        if (emailCliente == null || emailCliente.isBlank()) {
            return;
        }

        emailService.enviarEmailVeiculoProntoRetirada(
                emailCliente,
                nomeCliente,
                placa,
                marca,
                modelo,
                concluidoEm,
                checklistId,
                servicoId);
    }
}