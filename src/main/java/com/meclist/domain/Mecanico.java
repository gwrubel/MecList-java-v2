package com.meclist.domain;
import java.time.LocalDateTime;

import com.meclist.domain.enums.Situacao;
import com.meclist.domain.enums.TipoDeUsuario;

public class Mecanico extends Usuario {

    private String cpf;
    private String telefone;

    // Construtor para reconstrução do Mecânico a partir do banco
    public Mecanico(String cpf, String telefone, Long id, String nome, String email, String senhaHash,
                    LocalDateTime criadoEm, LocalDateTime atualizadoEm, Situacao situacao) {
        super(id, nome, email, senhaHash, TipoDeUsuario.MECANICO, criadoEm, atualizadoEm, situacao);
        this.cpf = cpf;
        this.telefone = telefone;
    }

    // Construtor privado para uso interno (utilizado no método de fábrica)
    private Mecanico(String cpf, String telefone, Long id, String nome, String email, String senhaHash, Situacao situacao) {
        super(id, nome, email, senhaHash, TipoDeUsuario.MECANICO, situacao);
        this.cpf = cpf;
        this.telefone = telefone;
    }

    // Método de fábrica para novo cadastro
    public static Mecanico novoCadastro(String cpf, String telefone, String nome, String email, String senhaHash) {
        return new Mecanico(cpf, telefone, null, nome, email, senhaHash, Situacao.ATIVO);
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

   
    public void atualizarTelefone(String novoTelefone) {
        this.telefone = novoTelefone;
        atualizarUltimaAtualizacao();
    }

    public void atualizarCpf (String cpf) {
        this.cpf = cpf;
        atualizarUltimaAtualizacao();
    }
}
