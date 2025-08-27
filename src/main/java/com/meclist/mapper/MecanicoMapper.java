package com.meclist.mapper;

import com.meclist.domain.Mecanico;
import com.meclist.dto.mecanico.MecanicoResponse;
import com.meclist.persistence.entity.MecanicoEntity;

public class MecanicoMapper {

    public static MecanicoEntity toEntity(Mecanico mecanico) {
        MecanicoEntity entity = new MecanicoEntity();
        entity.setId(mecanico.getId());
        entity.setNome(mecanico.getNome());
        entity.setEmail(mecanico.getEmail());
        entity.setSenha(mecanico.getSenha());
        entity.setCpf(mecanico.getCpf());
        entity.setTelefone(mecanico.getTelefone());
        entity.setTipoDeUsuario(mecanico.getTipoDeUsuario().name());
        entity.setCriadoEm(mecanico.getCriadoEm());
        entity.setAtualizadoEm(mecanico.getAtualizadoEm());
        entity.setSituacao(mecanico.getSituacao());
        return entity;
    }

    public static Mecanico toDomain(MecanicoEntity entity) {
        return new Mecanico(
            entity.getCpf(),
            entity.getTelefone(),
            entity.getId(),
            entity.getNome(),
            entity.getEmail(),
            entity.getSenha(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm(),
            entity.getSituacao()
            );
    }
    

    public static MecanicoResponse toResponse(Mecanico mecanico) {
        return new MecanicoResponse(
            mecanico.getId(),
            mecanico.getNome(),
            mecanico.getEmail(),
            mecanico.getCpf(),
            mecanico.getTelefone()
            ,mecanico.getSituacao().name()
        );
    }
    
}
