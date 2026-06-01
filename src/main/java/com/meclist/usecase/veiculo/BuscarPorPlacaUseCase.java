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
        String busca = termo == null ? "" : termo.trim();
        // Se termo vazio, retorna top 10 ordenadas por placa (sem filtro)
        if (busca.isEmpty()) {
            // Busca todas as placas, mas limitado a 10 (ordenado por placa)
            return veiculoGateway.buscarPlacasPorTrecho("")
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
        // Busca normal por termo
        return veiculoGateway.buscarPlacasPorTrecho(busca)
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
