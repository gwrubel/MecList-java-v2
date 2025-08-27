package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;

import com.meclist.domain.Veiculo;

public interface VeiculoGateway {
    public void salvarVeiculo(Veiculo veiculo);
    public void atualizarVeiculo(Veiculo veiculo);
    public Optional<Veiculo> buscarVeiculoPorId(Long id);
    List<String> buscarPlacasPorTrecho(String trecho);
    public Optional<Veiculo> buscarPorPlaca(String placa);

}
