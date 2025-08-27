package com.meclist.domain;

import java.time.LocalDateTime;

import com.meclist.domain.enums.Situacao;
import com.meclist.domain.enums.TipoDeUsuario;


public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private TipoDeUsuario tipoDeUsuario;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private Situacao situacao;
    
    public Usuario() {}

    public Usuario(Long id, String nome, String email, String senhaComHash, TipoDeUsuario tipoDeUsuario, Situacao situacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senhaComHash;
        this.tipoDeUsuario = tipoDeUsuario;
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        this.situacao = Situacao.ATIVO;
 
    }

    // Construtor alternativo para uso no banco de dados
    public Usuario(Long id, String nome, String email, String senhaHash, TipoDeUsuario tipoDeUsuario,
            LocalDateTime criadoEm, LocalDateTime atualizadoEm, Situacao Situacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senhaHash;
        this.tipoDeUsuario = tipoDeUsuario;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.situacao = Situacao;
    }
 

    public void atualizarSenhaComHash(String novaSenhaHash) {
        this.senha = novaSenhaHash;
        this.atualizadoEm = LocalDateTime.now();
    }
    

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public TipoDeUsuario getTipoDeUsuario() {
        return tipoDeUsuario;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
    public Situacao getSituacao() {
        return situacao;
    }
    
    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void desativar() {
        this.situacao = Situacao.INATIVO;
        this.atualizadoEm = LocalDateTime.now();
    }
    
    public void ativar() {
        this.situacao = Situacao.ATIVO;
        this.atualizadoEm = LocalDateTime.now();
    }
    protected void atualizarUltimaAtualizacao() {
        this.atualizadoEm = LocalDateTime.now();
    }
    
    public void atualizarNome(String nome) {
        this.nome = nome;
        this.atualizadoEm = LocalDateTime.now();
    }
    public void atualizarEmail(String email) {
        this.email = email;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

   

   
}
