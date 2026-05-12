package com.meclist.persistence.gateway;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.Servico;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.interfaces.ServicoGateway;
import com.meclist.mapper.ServicoMapper;
import com.meclist.persistence.repository.ServicoRepository;

@Component
public class ServicoGatewayImpl implements ServicoGateway {

    private final ServicoRepository servicoRepository;

    public ServicoGatewayImpl(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    @Override
    public Servico salvar(Servico servico) {
        var entity = ServicoMapper.toEntity(servico);
        var saved = servicoRepository.save(entity);
        return ServicoMapper.toDomain(saved);
    }

    @Override
    public Optional<Servico> buscarPorId(Long id) {
        return servicoRepository.findById(id).map(ServicoMapper::toDomain);
    }

    @Override
    public List<Servico> buscarPorChecklistId(Long checklistId) {
        return servicoRepository.findByChecklistIdOrderByIdDesc(checklistId)
                .stream()
                .map(ServicoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Servico> buscarPorMecanicoEStatuses(Long mecanicoId, Collection<StatusProcesso> statuses) {
        return servicoRepository.findByMecanicoIdAndStatusInOrderByAtualizadoEmDescIdDesc(mecanicoId, statuses)
                .stream()
                .map(ServicoMapper::toDomain)
                .toList();
    }
}
