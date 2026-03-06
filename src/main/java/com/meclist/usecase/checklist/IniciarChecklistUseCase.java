package com.meclist.usecase.checklist;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meclist.domain.Checklist;
import com.meclist.domain.Item;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.Mecanico;
import com.meclist.domain.Veiculo;
import com.meclist.domain.enums.StatusProcesso;
import com.meclist.dto.checklist.ChecklistResponse;
import com.meclist.dto.checklist.IniciarChecklistRequest;
import com.meclist.exception.ItemNaoEncontradoException;
import com.meclist.exception.MecanicoNaoEncontradoException;
import com.meclist.exception.VeiculoNaoEncontrado;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ItemChecklistGateway;
import com.meclist.interfaces.ItemGateway;
import com.meclist.interfaces.MecanicoGateway;
import com.meclist.interfaces.VeiculoGateway;
import com.meclist.mapper.ChecklistMapper;

@Service
public class IniciarChecklistUseCase {

    private final ChecklistGateway checklistGateway;
    private final ItemChecklistGateway itemChecklistGateway;
    private final VeiculoGateway veiculoGateway;
    private final MecanicoGateway mecanicoGateway;
    private final ItemGateway itemGateway;

    public IniciarChecklistUseCase(ChecklistGateway checklistGateway,
                                   ItemChecklistGateway itemChecklistGateway,
                                   VeiculoGateway veiculoGateway,
                                   MecanicoGateway mecanicoGateway,
                                   ItemGateway itemGateway) {
        this.checklistGateway = checklistGateway;
        this.itemChecklistGateway = itemChecklistGateway;
        this.veiculoGateway = veiculoGateway;
        this.mecanicoGateway = mecanicoGateway;
        this.itemGateway = itemGateway;
    }

    @Transactional
    public ChecklistResponse executar(IniciarChecklistRequest request) {
        // 1. Valida se o veículo existe
        Veiculo veiculo = veiculoGateway.buscarVeiculoPorId(request.idVeiculo())
                .orElseThrow(() -> new VeiculoNaoEncontrado("Veículo não encontrado: " + request.idVeiculo()));

        // 2. Valida se o mecânico existe
        Mecanico mecanico = mecanicoGateway.bucarPorId(request.idMecanico())
                .orElseThrow(() -> new MecanicoNaoEncontradoException("Mecânico não encontrado: " + request.idMecanico()));

        // 3. Atualiza e persiste a quilometragem do veículo
        veiculo.atualizarQuilometragem(veiculo.getQuilometragem(), request.quilometragem());
        Veiculo veiculoAtualizado = veiculoGateway.atualizarVeiculo(veiculo);

        // 4. Cria o checklist de domínio já associado ao veículo com KM atualizada
        Checklist checklist = Checklist.novo(
                veiculoAtualizado,
                mecanico,
                request.quilometragem(),
                request.descricao(),
                StatusProcesso.EM_ANDAMENTO
        );

        // 5. Salva o checklist
        Checklist checklistSalvo = checklistGateway.salvar(checklist);

        // 6. Busca TODOS os itens do catálogo
        List<Item> todosItens = itemGateway.buscarTodos();

        //revisar salvamento em lote para evitar loop de chamadas ao banco
       // 7. Prepara a lista de itens para inserção em lote
List<ItemChecklist> itensParaSalvar = todosItens.stream()
        .map(item -> ItemChecklist.novo(checklistSalvo, item))
        .toList();

// 7.1. Salva todos de uma vez (o Gateway deve suportar salvar uma lista)
itemChecklistGateway.salvarTodos(itensParaSalvar);

        // 8. Busca o checklist completo com os itens criados
        Checklist checklistCompleto = checklistGateway.buscarPorId(checklistSalvo.getId())
                .orElseThrow(() -> new ItemNaoEncontradoException("Erro ao buscar checklist criado"));

        
                

                // 9. Retorna o response agrupado por categoria
        return ChecklistMapper.toResponse(checklistCompleto);
    }
}