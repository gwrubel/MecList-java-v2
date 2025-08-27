package com.meclist.domain;

public class StatusItem {

    private Long id;
    private String descricao;

    public StatusItem(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public static StatusItem novo(String descricao) {
        return new StatusItem(null, descricao);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

