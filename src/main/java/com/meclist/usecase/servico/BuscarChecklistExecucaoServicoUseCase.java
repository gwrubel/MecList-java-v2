package com.meclist.usecase.servico;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.meclist.domain.ChecklistProduto;
import com.meclist.domain.ItemChecklist;
import com.meclist.domain.enums.StatusItem;
import com.meclist.dto.servico.ChecklistExecucaoServicoResponse;
import com.meclist.dto.servico.ItemExecucaoServicoResponse;
import com.meclist.dto.servico.ProdutoExecucaoServicoResponse;
import com.meclist.exception.AcessoNegadoException;
import com.meclist.exception.ChecklistNaoEncontradoException;
import com.meclist.exception.ServicoNaoEncontradoException;
import com.meclist.interfaces.ChecklistGateway;
import com.meclist.interfaces.ServicoGateway;
import com.meclist.security.AuthenticatedUserProvider;

@Service
public class BuscarChecklistExecucaoServicoUseCase {

    private final ServicoGateway servicoGateway;
    private final ChecklistGateway checklistGateway;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public BuscarChecklistExecucaoServicoUseCase(ServicoGateway servicoGateway,
                                                 ChecklistGateway checklistGateway,
                                                 AuthenticatedUserProvider authenticatedUserProvider) {
        this.servicoGateway = servicoGateway;
        this.checklistGateway = checklistGateway;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    public ChecklistExecucaoServicoResponse executar(Long servicoId) {
        var servico = servicoGateway.buscarPorId(servicoId)
                .orElseThrow(() -> new ServicoNaoEncontradoException(servicoId));

        validarAcesso(servico);

        Long checklistId = servico.getChecklist() != null ? servico.getChecklist().getId() : null;
        if (checklistId == null) {
            throw new ChecklistNaoEncontradoException("Checklist não encontrado para o serviço: " + servicoId);
        }

        var checklist = checklistGateway.buscarPorId(checklistId)
                .orElseThrow(() -> new ChecklistNaoEncontradoException("Checklist não encontrado: " + checklistId));

        Map<com.meclist.domain.enums.CategoriaParteVeiculo, List<ItemExecucaoServicoResponse>> itensPorCategoria = checklist
                .getItensChecklist().stream()
                .filter(item -> item.getStatusItem() == StatusItem.TROCAR)
                .map(this::toItemExecucao)
                .filter(item -> !item.produtosAutorizados().isEmpty())
                .collect(Collectors.groupingBy(ItemExecucaoServicoResponse::parteDoVeiculo));

        var veiculo = checklist.getVeiculo();
        return new ChecklistExecucaoServicoResponse(
                servico.getId(),
                checklist.getId(),
                servico.getStatus(),
                checklist.getStatus(),
                veiculo != null ? veiculo.getId() : null,
                veiculo != null ? veiculo.getPlaca() : null,
                veiculo != null ? veiculo.getCor() : null,
                veiculo != null ? veiculo.getMarca() : null,
                veiculo != null ? veiculo.getModelo() : null,
                veiculo != null ? veiculo.getAno() : null,
                checklist.getQuilometragem(),
                checklist.getDescricao(),
                itensPorCategoria);
    }

    private void validarAcesso(com.meclist.domain.Servico servico) {
        var user = authenticatedUserProvider.get();
        if ("MECANICO".equals(user.role())) {
            Long mecanicoDoServico = servico.getMecanico() != null ? servico.getMecanico().getId() : null;
            if (mecanicoDoServico == null || !mecanicoDoServico.equals(user.id())) {
                throw new AcessoNegadoException("Este serviço pertence a outro mecânico.");
            }
            return;
        }

        if (!"ADMIN".equals(user.role()) && !"ADM".equals(user.role())) {
            throw new AcessoNegadoException("Você não tem permissão para visualizar este serviço.");
        }
    }

    private ItemExecucaoServicoResponse toItemExecucao(ItemChecklist item) {
        List<ProdutoExecucaoServicoResponse> produtosAutorizados = item.getProdutosOrcados().stream()
                .filter(ChecklistProduto::estaAprovado)
                .map(produto -> new ProdutoExecucaoServicoResponse(
                        produto.getId(),
                        produto.getNomeProdutoSnapshot(),
                        produto.getQuantidade(),
                        produto.getMarca()))
                .toList();

        var parteDoVeiculo = item.getItem() != null ? item.getItem().getParteDoVeiculo() : null;
        var imagemIlustrativa = item.getItem() != null ? item.getItem().getImagemIlustrativa() : null;

        return new ItemExecucaoServicoResponse(
                item.getId(),
                item.getNomeItemSnapshot(),
            imagemIlustrativa,
                parteDoVeiculo,
                produtosAutorizados);
    }
}
