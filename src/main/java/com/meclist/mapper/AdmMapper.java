package com.meclist.mapper;

import com.meclist.domain.Adm;
import com.meclist.persistence.entity.AdmEntity;

public class AdmMapper {
    public static AdmEntity toEntity(Adm adm) {
        AdmEntity entity = new AdmEntity();
        entity.setId(adm.getId());
        entity.setEmail(adm.getEmail());
        entity.setSenha(adm.getSenha());
        entity.setTipoDeUsuario(adm.getTipoDeUsuario().name());
        entity.setNome(adm.getNome());
        entity.setCriadoEm(adm.getCriadoEm());
        entity.setAtualizadoEm(adm.getAtualizadoEm());
        entity.setSituacao(adm.getSituacao());
        return entity;
    }

    public static Adm toDomain (AdmEntity entity) {
        return new Adm(
            entity.getId(),
            entity.getNome(),
            entity.getEmail(),
            entity.getSenha(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm(),
            entity.getSituacao()
        );
    }

}
