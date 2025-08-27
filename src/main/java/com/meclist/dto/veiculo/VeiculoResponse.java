package com.meclist.dto.veiculo;

import java.time.LocalDate;

public class VeiculoResponse {
    private Long id;
    private String placa;
    private String modelo;
    private String marca;
    private String cor;
    private Integer ano;
    private float quilometragem;
    private LocalDate dataUltimaRevisao ;

    public VeiculoResponse(Long id, String placa, String modelo, String marca, String cor, Integer ano, float quilometragem, LocalDate dataUltimaRevisao) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.cor = cor;
        this.ano = ano;
        this.quilometragem = quilometragem;
        this.dataUltimaRevisao = dataUltimaRevisao;
    }

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMarca() {
        return marca;
    }

    public String getCor() {
        return cor;
    }

    public Integer getAno() {
        return ano;
    }

    public float getQuilometragem() {
        return quilometragem;
    }

    public LocalDate getDataUltimaRevisao() {
        return dataUltimaRevisao;
    }
}
