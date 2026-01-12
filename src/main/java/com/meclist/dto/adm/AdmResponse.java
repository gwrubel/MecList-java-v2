package com.meclist.dto.adm;

import com.meclist.domain.Adm;

public record AdmResponse(
    Long id,
    String nome,
    String email
) {
    public static AdmResponse fromEntity (Adm adm) {
        return new AdmResponse(
            adm.getId(),
            adm.getNome(),
            adm.getEmail()
        );
    }
}
