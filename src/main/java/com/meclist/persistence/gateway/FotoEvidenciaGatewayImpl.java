package com.meclist.persistence.gateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.meclist.domain.FotoEvidencia;
import com.meclist.interfaces.FotoEvidenciaGateway;
import com.meclist.mapper.FotoEvidenciaMapper;
import com.meclist.persistence.entity.FotoEvidenciaEntity;
import com.meclist.persistence.entity.ItemChecklistEntity;
import com.meclist.persistence.repository.FotoEvidenciaRepository;
import com.meclist.persistence.repository.ItemChecklistRepository;

@Component
public class FotoEvidenciaGatewayImpl implements FotoEvidenciaGateway {
    
    private final FotoEvidenciaRepository fotoEvidenciaRepository;
    private final ItemChecklistRepository itemChecklistRepository;
    
    public FotoEvidenciaGatewayImpl(FotoEvidenciaRepository fotoEvidenciaRepository,
                                   ItemChecklistRepository itemChecklistRepository) {
        this.fotoEvidenciaRepository = fotoEvidenciaRepository;
        this.itemChecklistRepository = itemChecklistRepository;
    }
    
    @Override
    public FotoEvidencia salvar(FotoEvidencia foto) {
        // Busca o ItemChecklist para associar à foto
        ItemChecklistEntity itemChecklistEntity = itemChecklistRepository
                .findById(foto.getItemChecklist().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "ItemChecklist não encontrado: " + foto.getItemChecklist().getId()));
        
        // Converte domain → entity usando o mapper (já seta o ItemChecklist)
        FotoEvidenciaEntity entity = FotoEvidenciaMapper.toEntity(foto, itemChecklistEntity);

        // Salva no banco
        FotoEvidenciaEntity entitySalva = fotoEvidenciaRepository.save(entity);
        
        // Converte entity → domain e retorna
        return FotoEvidenciaMapper.toDomain(entitySalva);
    }
    
    @Override
    public Optional<FotoEvidencia> buscarPorId(Long id) {
        return fotoEvidenciaRepository.findById(id)
                .map(FotoEvidenciaMapper::toDomain);
    }
    
    @Override
    public List<FotoEvidencia> buscarPorItemChecklist(Long idItemChecklist) {
        return fotoEvidenciaRepository.findByItemChecklistId(idItemChecklist)
                .stream()
                .map(FotoEvidenciaMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deletar(Long id) {
        fotoEvidenciaRepository.deleteById(id);
    }
    
    @Override
    public void deletarPorIds(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            fotoEvidenciaRepository.deleteAllByIdInBatch(ids);
            fotoEvidenciaRepository.flush();
        }
    }
}
