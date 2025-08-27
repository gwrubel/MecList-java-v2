package com.meclist.domain;

import java.time.LocalDateTime;

public class Status {
    private Long id;
    private String descricao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Status(Long id, String descricao, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.descricao = descricao;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Status novo(String descricao) {
        LocalDateTime agora = LocalDateTime.now();
        return new Status(null, descricao, agora, agora);
    }

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}
