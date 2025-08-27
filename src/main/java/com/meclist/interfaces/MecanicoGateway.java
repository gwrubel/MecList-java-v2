package com.meclist.interfaces;

import java.util.List;
import java.util.Optional;

import com.meclist.domain.Mecanico;
import com.meclist.domain.enums.Situacao;


public interface MecanicoGateway {

    public void salvar(Mecanico mecanico);
    public Optional<Mecanico> buscarPorEmail(String email);
    public Optional<Mecanico> buscarPorCpf(String cpf);
    List <Mecanico> buscarTodos();
    public Optional <Mecanico> bucarPorId(Long id);
    public void atualizarMecanico(Mecanico mecanico);
    List<Mecanico> buscarPorSituacao(Situacao situacao);

}
