package com.meclist.interfaces;

import java.util.Optional;

import com.meclist.domain.Adm;

public interface AdmGateway {
    
    public Adm cadastrarAdm(Adm adm);
    Optional <Adm> buscarPorEmail(String email);
    
}
