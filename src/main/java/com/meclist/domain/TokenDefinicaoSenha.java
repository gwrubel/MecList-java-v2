package com.meclist.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class TokenDefinicaoSenha {
    private Long id;
    private String token;
    private String email;
    private boolean usado;
    private LocalDateTime expiraEm;
    private LocalDateTime criadoEm;

    public TokenDefinicaoSenha() {}

    public static TokenDefinicaoSenha gerar(String email, int horasValidade) {
        TokenDefinicaoSenha t = new TokenDefinicaoSenha();
        t.token = UUID.randomUUID().toString();
        t.email = email;
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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isUsado() { return usado; }
    public void setUsado(boolean usado) { this.usado = usado; }
    public LocalDateTime getExpiraEm() { return expiraEm; }
    public void setExpiraEm(LocalDateTime expiraEm) { this.expiraEm = expiraEm; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
