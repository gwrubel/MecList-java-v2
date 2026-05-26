package com.meclist.interfaces;

import java.time.LocalDateTime;

public interface NotificacaoGateway {

    void notificarVeiculoProntoRetirada(Long servicoId,
                                        Long checklistId,
                                        String emailCliente,
                                        String nomeCliente,
                                        String placa,
                                        String marca,
                                        String modelo,
                                        LocalDateTime concluidoEm);
}