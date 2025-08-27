
package com.meclist.domain.factory;



import com.meclist.domain.Cliente;
import com.meclist.domain.Veiculo;

public class VeiculoFactory {

    public static Veiculo criarNovoVeiculo(
        String placa,
        String marca,
        String modelo,
        Integer ano,
        String cor,
        float quilometragem,
        Cliente cliente
    ) {
        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("Placa não pode ser nula ou vazia");
        }

        if (quilometragem < 0) {
            throw new IllegalArgumentException("Quilometragem não pode ser negativa");
        }

        return new Veiculo(
            null,
            placa,
            marca,
            modelo,
            ano,
            cor,
            quilometragem,
            cliente
        );
    }
}

