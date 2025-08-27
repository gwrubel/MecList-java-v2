package com.meclist.domain;

import java.time.LocalDateTime;

import com.meclist.domain.enums.Situacao;
import com.meclist.domain.enums.TipoDeUsuario;

public class Adm extends Usuario {

    // Construtor para criação de novo ADM (senha pura, será hasheada)
    public Adm(Long id, String nome, String email, String senhaPura, Situacao situacao) {
        super(id, nome, email, senhaPura, TipoDeUsuario.ADM, situacao); // FALTAVA o 'situacao' aqui
    }

    // Construtor para reconstrução do ADM a partir do banco (senha já hasheada)
    public Adm(Long id, String nome, String email, String senhaHash,
               LocalDateTime criadoEm, LocalDateTime atualizadoEm, Situacao situacao) {
        super(id, nome, email, senhaHash, TipoDeUsuario.ADM, criadoEm, atualizadoEm, situacao);
    }

    public static Adm novoCadastro(String nome, String email, String senhaHash) {
        return new Adm(null, nome, email, senhaHash, Situacao.ATIVO);
    }
    
}
