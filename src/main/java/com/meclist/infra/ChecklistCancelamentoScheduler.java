package com.meclist.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.meclist.usecase.checklist.CancelarChecklistsInativosUseCase;

@Component
public class ChecklistCancelamentoScheduler {

    private static final Logger log = LoggerFactory.getLogger(ChecklistCancelamentoScheduler.class);

    private final CancelarChecklistsInativosUseCase useCase;

    public ChecklistCancelamentoScheduler(CancelarChecklistsInativosUseCase useCase) {
        this.useCase = useCase;
    }

    @Scheduled(cron = "0 0 16 * * *")
    public void cancelarInativosScheduled() {
        int total = useCase.executar();
        log.info("Cancelamento automático: {} checklist(s) cancelado(s) por inatividade.", total);
    }
}
