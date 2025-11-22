package com.meclist.dto.veiculo;

public record AtualizarVeiculoRequestDTO(
        String placa,
        String modelo,
        String marca,
        String cor,
        Integer ano,
        Float quilometragem
) {}
