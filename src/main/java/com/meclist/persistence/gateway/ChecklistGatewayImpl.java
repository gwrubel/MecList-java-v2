package com.meclist.persistence.gateway;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.meclist.domain.Checklist;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.persistence.entity.ChecklistEntity;
import com.meclist.persistence.entity.MecanicoEntity;
import com.meclist.persistence.entity.StatusEntity;
import com.meclist.persistence.repository.ChecklistRepository;
import com.meclist.persistence.repository.MecanicoRepository;
import com.meclist.persistence.repository.StatusRepository;
import com.meclist.persistence.repository.VeiculoRepository;

@Component
public class ChecklistGatewayImpl implements ChecklistGateway {
    private final ChecklistRepository checklistRepository;
    private final VeiculoRepository veiculoRepository;
    private final MecanicoRepository mecanicoRepository;
    private final StatusRepository statusRepository;

    public ChecklistGatewayImpl(ChecklistRepository checklistRepository, 
                              VeiculoRepository veiculoRepository,
                              MecanicoRepository mecanicoRepository,
                              StatusRepository statusRepository) {
        this.checklistRepository = checklistRepository;
        this.veiculoRepository = veiculoRepository;
        this.mecanicoRepository = mecanicoRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public Checklist salvar(Checklist checklist) {
        var entity = new ChecklistEntity();
        entity.setId(checklist.getId());
        entity.setVeiculo(veiculoRepository.findById(checklist.getIdVeiculo()).orElseThrow());
        
        // Buscar o mecânico se o ID não for nulo
        if (checklist.getIdMecanico() != null) {
            MecanicoEntity mecanico = mecanicoRepository.findById(checklist.getIdMecanico()).orElse(null);
            entity.setMecanico(mecanico);
        }
        
        entity.setQuilometragem(checklist.getQuilometragem());
        entity.setDescricao(checklist.getDescricao());
        
        // Buscar o status (agora usando StatusEntity)
        StatusEntity status = statusRepository.findById(1L).orElseThrow(); // Status padrão (Pendente)
        entity.setStatus(status);
        
        entity.setCriadoEm(checklist.getCriadoEm());
        entity.setAtualizadoEm(checklist.getAtualizadoEm());

        var salvo = checklistRepository.save(entity);
        return new Checklist(
                salvo.getId(),
                salvo.getVeiculo().getId(),
                salvo.getMecanico() != null ? salvo.getMecanico().getId() : null,
                salvo.getQuilometragem(),
                salvo.getDescricao(),
                salvo.getStatus().getId(),
                salvo.getCriadoEm(),
                salvo.getAtualizadoEm()
        );
    }

    @Override
    public Optional<Checklist> buscarPorId(Long id) {
        return checklistRepository.findById(id).map(salvo -> new Checklist(
                salvo.getId(),
                salvo.getVeiculo().getId(),
                salvo.getMecanico() != null ? salvo.getMecanico().getId() : null,
                salvo.getQuilometragem(),
                salvo.getDescricao(),
                salvo.getStatus().getId(),
                salvo.getCriadoEm(),
                salvo.getAtualizadoEm()
        ));
    }
}



