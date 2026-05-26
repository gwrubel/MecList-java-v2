package com.meclist.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meclist.domain.enums.StatusNotificacao;
import com.meclist.persistence.entity.NotificacaoOutboxEntity;

@Repository
public interface NotificacaoOutboxRepository extends JpaRepository<NotificacaoOutboxEntity, Long> {

    List<NotificacaoOutboxEntity> findByStatusAndTentativasLessThan(StatusNotificacao status, int maxTentativas);
}
