package com.meclist.mapper;

import java.time.LocalDateTime;

import com.meclist.domain.Cliente;
import com.meclist.domain.Veiculo;
import com.meclist.dto.veiculo.VeiculoRequestDTO;
import com.meclist.dto.veiculo.VeiculoResponse;
import com.meclist.persistence.entity.ClienteEntity;
import com.meclist.persistence.entity.VeiculoEntity;

public class VeiculoMapper {
    
    // Converte domínio → entidade (precisa do ClienteEntity)
    public static VeiculoEntity toEntity(Veiculo veiculo, ClienteEntity clienteEntity) {
        VeiculoEntity entity = new VeiculoEntity();
        entity.setId(veiculo.getId());
        entity.setPlaca(veiculo.getPlaca());
        entity.setMarca(veiculo.getMarca());
        entity.setModelo(veiculo.getModelo());
        entity.setAno(veiculo.getAno());
        entity.setCor(veiculo.getCor());
        entity.setQuilometragem(veiculo.getQuilometragem());
        entity.setCriadoEm(veiculo.getCriadoEm());
        entity.setAtualizadoEm(veiculo.getAtualizadoEm());
        entity.setCliente(clienteEntity);
        return entity;
    }

    // Converte domínio → entidade (busca o cliente do próprio domínio)
    public static VeiculoEntity toEntity(Veiculo veiculo) {
        VeiculoEntity entity = new VeiculoEntity();
        entity.setId(veiculo.getId());
        entity.setPlaca(veiculo.getPlaca());
        entity.setMarca(veiculo.getMarca());
        entity.setModelo(veiculo.getModelo());
        entity.setAno(veiculo.getAno());
        entity.setCor(veiculo.getCor());
        entity.setQuilometragem(veiculo.getQuilometragem());
        entity.setCriadoEm(veiculo.getCriadoEm());
        entity.setAtualizadoEm(veiculo.getAtualizadoEm());
        
        // Converte o cliente do domínio para entidade
        if (veiculo.getCliente() != null) {
            entity.setCliente(ClienteMapper.toEntity(veiculo.getCliente()));
        }
        
        return entity;
    }

    // Converte entidade → domínio (com cliente passado)
    public static Veiculo toDomain(VeiculoEntity entity, Cliente cliente) {
        return new Veiculo(
            entity.getId(),
            entity.getPlaca(),
            entity.getMarca(),
            entity.getModelo(),
            entity.getAno(),
            entity.getCor(),
            entity.getQuilometragem(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm(),
            null,
            cliente
        );
    }

    // Converte entidade → domínio (busca cliente da própria entidade)
    public static Veiculo toDomain(VeiculoEntity entity) {
        Cliente cliente = entity.getCliente() != null 
            ? ClienteMapper.toDomain(entity.getCliente())
            : null;
            
        return new Veiculo(
            entity.getId(),
            entity.getPlaca(),
            entity.getMarca(),
            entity.getModelo(),
            entity.getAno(),
            entity.getCor(),
            entity.getQuilometragem(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm(),
            null,
            cliente
        );
    }

    public static Veiculo toDomain(VeiculoRequestDTO request, Cliente cliente) {
        return new Veiculo(
            null,
            request.placa(),
            request.marca(),
            request.modelo(),
            request.ano(),
            request.cor(),
            request.quilometragem(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            cliente
        );
    }

    public static VeiculoResponse toResponse(Veiculo veiculo) {
        return new VeiculoResponse(
            veiculo.getId(),
            veiculo.getPlaca(),
            veiculo.getModelo(),
            veiculo.getMarca(),
            veiculo.getCor(),
            veiculo.getAno(),
            veiculo.getQuilometragem(),
            veiculo.getDataUltimaRevisao()
        );
    }
}
