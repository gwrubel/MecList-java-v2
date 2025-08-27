package com.meclist.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.meclist.domain.Cliente;
import com.meclist.domain.Veiculo;
import com.meclist.dto.cliente.ClienteResponse;
import com.meclist.dto.veiculo.VeiculoResponse;
import com.meclist.persistence.entity.ClienteEntity;
import com.meclist.persistence.entity.VeiculoEntity;

public class ClienteMapper {
    


    public static ClienteEntity toEntity(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();
        entity.setId(cliente.getId());
        entity.setNome(cliente.getNome());
        entity.setEmail(cliente.getEmail());
        entity.setSenha(cliente.getSenha());
        entity.setDocumento(cliente.getDocumento());
        entity.setTipoDocumento(cliente.getTipoDocumento());
        entity.setTelefone(cliente.getTelefone());
        entity.setTipoDeUsuario(cliente.getTipoDeUsuario().name());
        entity.setCriadoEm(cliente.getCriadoEm());
        entity.setAtualizadoEm(cliente.getAtualizadoEm());
        entity.setSituacao(cliente.getSituacao());
        entity.setEndereco(cliente.getEndereco());
        List<VeiculoEntity> veiculoEntities = cliente.getVeiculos().stream()
        .map(veiculo -> VeiculoMapper.toEntity(veiculo, entity))
        .collect(Collectors.toList());
        entity.setVeiculos(veiculoEntities);
        return entity;
    }

   public static Cliente toDomain(ClienteEntity entity ) {
    // Primeiro, criamos o cliente sem os veículos
    Cliente cliente = new Cliente(
        entity.getId(),
        entity.getNome(),
        entity.getEmail(),
        entity.getSenha(),
        entity.getDocumento(),
        entity.getTipoDocumento(),
        entity.getTelefone(),
        entity.getEndereco(),
        entity.getSituacao(),
        new ArrayList<>(), // Inicializa a lista de veículos vazia
        entity.getCriadoEm(),
        entity.getAtualizadoEm()
    );

    // Agora mapeamos os veículos passando o cliente recém-criado
    List<Veiculo> veiculos = entity.getVeiculos().stream()
        .map(veiculoEntity -> VeiculoMapper.toDomain(veiculoEntity, cliente))
        .collect(Collectors.toList());

    
    cliente.setVeiculos(veiculos);

    return cliente;
}




public static ClienteResponse toResponse(Cliente cliente) {
    List<VeiculoResponse> veiculosResponse = cliente.getVeiculos().stream()
        .map(v -> new VeiculoResponse(
            v.getId(),
            v.getPlaca(),
            v.getModelo(),
            v.getMarca(),
            v.getCor(),
            v.getAno(),
            v.getQuilometragem(),
            v.getDataUltimaRevisao()
        ))
        .collect(Collectors.toList());

    return new ClienteResponse(
        cliente.getId(),
        cliente.getNome(),
        cliente.getEmail(),
        cliente.getDocumento(),
        cliente.getTipoDocumento(),
        cliente.getTelefone(),
        cliente.getEndereco(),
        cliente.getTipoDeUsuario(),
        cliente.getSituacao(),
        cliente.getCriadoEm(),
        cliente.getAtualizadoEm(),
        veiculosResponse
    );
}

}