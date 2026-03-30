package com.meclist.persistence.gateway;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.meclist.domain.Orcamento;
import com.meclist.interfaces.OrcamentoGateway;
import com.meclist.mapper.OrcamentoMapper;
import com.meclist.persistence.repository.OrcamentoRepository;

@Component
public class OrcamentoGatewayImpl implements OrcamentoGateway {

    private final OrcamentoRepository orcamentoRepository;

    public OrcamentoGatewayImpl(OrcamentoRepository orcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
    }

    @Override
    public Orcamento salvar(Orcamento orcamento) {
        var entity = OrcamentoMapper.toEntity(orcamento);
        var salvo = orcamentoRepository.save(entity);
        return OrcamentoMapper.toDomain(salvo);
    }

    @Override
    public Optional<Orcamento> buscarPorChecklistId(Long checklistId) {
        return orcamentoRepository.findByChecklistId(checklistId)
                .map(OrcamentoMapper::toDomain);
    }
}
