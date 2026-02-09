package com.meclist.usecase.veiculo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meclist.dto.veiculo.VeiculoBuscaResponse;
import com.meclist.dto.veiculo.VeiculoResponse;
import com.meclist.exception.VeiculoNaoEncontrado;
import com.meclist.interfaces.VeiculoGateway;
import com.meclist.mapper.VeiculoMapper;


@Service
public class BuscarPorPlacaUseCase {
     private final VeiculoGateway veiculoGateway;

    public BuscarPorPlacaUseCase(VeiculoGateway veiculoGateway) {
        this.veiculoGateway = veiculoGateway;
    }

    
    public List<VeiculoBuscaResponse> buscarPlacaPorTermo(String termo) {
    return veiculoGateway.buscarPlacasPorTrecho(termo)
            .stream()
            .map(veiculo -> new VeiculoBuscaResponse(
                    veiculo.getId(),
                    veiculo.getPlaca(),
                    veiculo.getModelo(),
                    veiculo.getMarca(),
                    veiculo.getCor(),
                    veiculo.getAno(),
                    veiculo.getQuilometragem(),
                    veiculo.getDataUltimaRevisao()
            ))
            .toList();
}

    public VeiculoResponse buscarPorPlaca(String placa) {
        return veiculoGateway.buscarPorPlaca(placa)
                .map(VeiculoMapper::toResponse)
                .orElseThrow(() -> new VeiculoNaoEncontrado("Veículo não encontrado com a placa: " + placa));
    }
}
