package com.meclist.dto.veiculo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Positive;

public record VeiculoRequestDTO(
        @NotBlank(message = "A placa é obrigatória") String placa,
        @NotBlank(message = "O modelo é obrigatório") String modelo,
        @NotBlank(message = "A marca é obrigatória") String marca,
        @NotBlank(message = "A cor é obrigatória") String cor,

        @NotNull(message = "O ano é obrigatório") @Min(value = 1000, message = "Ano inválido! Deve conter 4 dígitos") @Max(value = 9999, message = "Ano inválido! Deve conter 4 dígitos") Integer ano,

        @Positive(message = "A quilometragem deve ser maior que zero") @NotNull(message = "A quilometragem é obrigatória") float quilometragem) {

    public String placa() {
        return placa;
    }

    public String modelo() {
        return modelo;
    }

    public String marca() {
        return marca;
    }

    public String cor() {
        return cor;
    }

    public Integer ano() {
        return ano;
    }

    public float quilometragem() {
        return quilometragem;
    }
}
