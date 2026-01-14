package com.meclist.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import com.meclist.exception.CampoInvalidoException;

public class Veiculo {

    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private Integer ano;
    private String cor;
    private float quilometragem;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private LocalDate dataUltimaRevisao;

    private Cliente cliente;

public Veiculo(Long id, String placa, String marca, String modelo, Integer ano, String cor, float quilometragem, Cliente cliente) {
    int anoAtual = LocalDateTime.now().getYear();
    if (ano > anoAtual) {
        throw new IllegalArgumentException("O ano do veículo não pode ser maior que o ano atual.");
    }
    this.id = id;
    this.placa = placa;
    this.marca = marca;
    this.modelo = modelo;
    this.ano = ano;
    this.cor = cor;
    this.quilometragem = quilometragem;
    this.criadoEm = LocalDateTime.now();
    this.atualizadoEm = LocalDateTime.now();
    this.cliente = cliente;
    this.dataUltimaRevisao = null; // Sem revisão no cadastro inicial
}



    /// Construtor para reconstrução do Veículo a partir do banco
  public Veiculo(Long id, String placa, String marca, String modelo, Integer ano, String cor, float quilometragem,
               LocalDateTime criadoEm, LocalDateTime atualizadoEm, LocalDate dataUltimaRevisao, Cliente cliente) {
    int anoAtual = LocalDateTime.now().getYear();
    if (ano > anoAtual) {
        throw new IllegalArgumentException("O ano do veículo não pode ser maior que o ano atual.");
    }
    this.id = id;
    this.placa = placa;
    this.marca = marca;
    this.modelo = modelo;
    this.ano = ano;
    this.cor = cor;
    this.quilometragem = quilometragem;
    this.criadoEm = criadoEm;
    this.atualizadoEm = atualizadoEm;
    this.dataUltimaRevisao = dataUltimaRevisao;
    this.cliente = cliente;
}


    public void atualizarQuilometragem(float kmAtual, float novaKm) {
        if (novaKm < kmAtual) {
            throw new CampoInvalidoException("quilometragem", "A quilometragem não pode ser menor que a atual.");
        }
        this.quilometragem = novaKm;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarPlaca(String novaPlaca) {
        this.placa = novaPlaca;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarMarca(String novaMarca) {
        this.marca = novaMarca;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarModelo(String novoModelo) {
        this.modelo = novoModelo;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarAno(Integer novoAno) {
        int anoAtual = LocalDateTime.now().getYear();
        if (novoAno > anoAtual) {
            throw new IllegalArgumentException("O ano do veículo não pode ser maior que o ano atual.");
        }
        this.ano = novoAno;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarCor(String novaCor) {
        this.cor = novaCor;
        this.atualizadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public String getCor() {
        return cor;
    }

    public float getQuilometragem() {
        return quilometragem;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void atualizarDataUltimaRevisao(LocalDate data) {
    this.dataUltimaRevisao = data;
    this.atualizadoEm = LocalDateTime.now();
}

public LocalDate getDataUltimaRevisao() {
    return dataUltimaRevisao;
}


}
