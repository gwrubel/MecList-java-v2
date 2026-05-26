package com.meclist.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.enums.StatusNotificacao;
import com.meclist.interfaces.NotificacaoGateway;
import com.meclist.persistence.entity.NotificacaoOutboxEntity;
import com.meclist.persistence.repository.NotificacaoOutboxRepository;

@Component
public class NotificacaoOutboxScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificacaoOutboxScheduler.class);
    private static final int MAX_TENTATIVAS = 3;

    private final NotificacaoOutboxRepository outboxRepository;
    private final NotificacaoGateway notificacaoGateway;

    public NotificacaoOutboxScheduler(NotificacaoOutboxRepository outboxRepository,
                                       NotificacaoGateway notificacaoGateway) {
        this.outboxRepository = outboxRepository;
        this.notificacaoGateway = notificacaoGateway;
    }

    @Scheduled(fixedDelayString = "${notificacao.outbox.intervalo-ms:300000}")
    @Transactional
    public void reprocessarPendentes() {
        var pendentes = outboxRepository.findByStatusAndTentativasLessThan(
                StatusNotificacao.PENDENTE, MAX_TENTATIVAS);

        if (pendentes.isEmpty()) {
            return;
        }

        LOGGER.info("Outbox: reprocessando {} notificação(ões) pendente(s).", pendentes.size());

        for (NotificacaoOutboxEntity entry : pendentes) {
            reenviar(entry);
        }
    }

    private void reenviar(NotificacaoOutboxEntity entry) {
        try {
            notificacaoGateway.notificarVeiculoProntoRetirada(
                    entry.getServicoId(),
                    entry.getChecklistId(),
                    entry.getEmailCliente(),
                    entry.getNomeCliente(),
                    entry.getPlaca(),
                    entry.getMarca(),
                    entry.getModelo(),
                    entry.getConcluidoEm());

            entry.setStatus(StatusNotificacao.ENVIADO);
            outboxRepository.save(entry);
            LOGGER.info("Outbox: notificação enviada com sucesso. id={}, servicoId={}", entry.getId(), entry.getServicoId());
        } catch (RuntimeException ex) {
            entry.setTentativas(entry.getTentativas() + 1);

            if (entry.getTentativas() >= MAX_TENTATIVAS) {
                entry.setStatus(StatusNotificacao.FALHA_DEFINITIVA);
                LOGGER.error("Outbox: falha definitiva após {} tentativas. id={}, servicoId={}, email={}",
                        MAX_TENTATIVAS, entry.getId(), entry.getServicoId(), entry.getEmailCliente(), ex);
            } else {
                LOGGER.warn("Outbox: falha na tentativa {}/{}. id={}, servicoId={}",
                        entry.getTentativas(), MAX_TENTATIVAS, entry.getId(), entry.getServicoId());
            }
            outboxRepository.save(entry);
        }
    }
}
