package com.meclist.usecase.cliente;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meclist.dto.cliente.VeiculoResumo;
import com.meclist.exception.AcessoNegadoException;
import com.meclist.interfaces.VeiculoGateway;
import com.meclist.security.AuthenticatedUserProvider;

@Service
public class ListarVeiculosClienteUseCase {

    private final VeiculoGateway veiculoGateway;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public ListarVeiculosClienteUseCase(VeiculoGateway veiculoGateway,
                                        AuthenticatedUserProvider authenticatedUserProvider) {
        this.veiculoGateway = veiculoGateway;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    public List<VeiculoResumo> executar() {
        var user = authenticatedUserProvider.get();
        if (!"CLIENTE".equals(user.role())) {
            throw new AcessoNegadoException("Apenas clientes podem listar seus veículos.");
        }

        return veiculoGateway.buscarVeiculosPorCliente(user.id())
                .stream()
                .map(v -> new VeiculoResumo(
                        v.getId(),
                        v.getMarca(),
                        v.getModelo(),
                        v.getPlaca(),
                        v.getAno(),
                        v.getQuilometragem(),
                        v.getDataUltimaRevisao()))
                .toList();
    }
}
