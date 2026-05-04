package com.meclist.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class TokenAprovacaoChecklist {

    private Long id;
    private String token;
    private Long clienteId;
    private Long checklistId;
    private boolean usado;
    private LocalDateTime expiraEm;
    private LocalDateTime criadoEm;

    public static TokenAprovacaoChecklist gerar(Long clienteId, Long checklistId, int horasValidade) {
        TokenAprovacaoChecklist t = new TokenAprovacaoChecklist();
        t.token = UUID.randomUUID().toString();
        t.clienteId = clienteId;
        t.checklistId = checklistId;
        t.usado = false;
        t.criadoEm = LocalDateTime.now();
        t.expiraEm = t.criadoEm.plusHours(horasValidade);
        return t;
    }

    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(expiraEm);
    }

    public boolean isValido() {
        return !usado && !isExpirado();
    }

    public void marcarComoUsado() {
        this.usado = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    public LocalDateTime getExpiraEm() {
        return expiraEm;
    }

    public void setExpiraEm(LocalDateTime expiraEm) {
        this.expiraEm = expiraEm;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}