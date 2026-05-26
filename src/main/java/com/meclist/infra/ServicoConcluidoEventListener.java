package com.meclist.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.meclist.domain.enums.StatusNotificacao;
import com.meclist.interfaces.NotificacaoGateway;
import com.meclist.persistence.entity.NotificacaoOutboxEntity;
import com.meclist.persistence.repository.NotificacaoOutboxRepository;

@Component
public class ServicoConcluidoEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServicoConcluidoEventListener.class);

    private final NotificacaoGateway notificacaoGateway;
    private final NotificacaoOutboxRepository outboxRepository;

    public ServicoConcluidoEventListener(NotificacaoGateway notificacaoGateway,
                                          NotificacaoOutboxRepository outboxRepository) {
        this.notificacaoGateway = notificacaoGateway;
        this.outboxRepository = outboxRepository;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onServicoConcluido(ServicoConcluidoEvent event) {
        if (event.emailCliente() == null || event.emailCliente().isBlank()) {
            LOGGER.warn("Notificação ignorada: e-mail ausente. servicoId={}, checklistId={}",
                    event.servicoId(), event.checklistId());
            return;
        }

        try {
            notificacaoGateway.notificarVeiculoProntoRetirada(
                    event.servicoId(),
                    event.checklistId(),
                    event.emailCliente(),
                    event.nomeCliente(),
                    event.placa(),
                    event.marca(),
                    event.modelo(),
                    event.concluidoEm());
            LOGGER.info("Notificação de retirada enviada. servicoId={}, checklistId={}",
                    event.servicoId(), event.checklistId());
        } catch (RuntimeException ex) {
            LOGGER.error("Falha ao enviar notificação de retirada. Salvando no outbox. servicoId={}, checklistId={}, email={}",
                    event.servicoId(), event.checklistId(), event.emailCliente(), ex);
            salvarNoOutbox(event);
        }
    }

    private void salvarNoOutbox(ServicoConcluidoEvent event) {
        var outbox = new NotificacaoOutboxEntity();
        outbox.setServicoId(event.servicoId());
        outbox.setChecklistId(event.checklistId());
        outbox.setEmailCliente(event.emailCliente());
        outbox.setNomeCliente(event.nomeCliente());
        outbox.setPlaca(event.placa());
        outbox.setMarca(event.marca());
        outbox.setModelo(event.modelo());
        outbox.setConcluidoEm(event.concluidoEm());
        outbox.setStatus(StatusNotificacao.PENDENTE);
        outbox.setTentativas(1);
        outboxRepository.save(outbox);
    }
}
