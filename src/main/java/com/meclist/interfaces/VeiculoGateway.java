package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;

import com.meclist.domain.Veiculo;

public interface VeiculoGateway {
    public Veiculo salvarVeiculo(Veiculo veiculo);
    public Veiculo atualizarVeiculo(Veiculo veiculo);
    public Optional<Veiculo> buscarVeiculoPorId(Long id);
    List<String> buscarPlacasPorTrecho(String trecho);
    public Optional<Veiculo> buscarPorPlaca(String placa);
    List<Veiculo> buscarVeiculosPorCliente(Long clienteId);

}
