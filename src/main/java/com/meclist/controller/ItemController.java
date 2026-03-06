package com.meclist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.domain.enums.Situacao;
import com.meclist.dto.item.AtualizarItemRequest;
import com.meclist.dto.item.CadastrarItemRequest;
import com.meclist.dto.item.ItemResponse;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.item.AtualizarItemUseCase;
import com.meclist.usecase.item.CadastrarItemUseCase;
import com.meclist.usecase.item.AlterarSituacaoDoItemUseCase;
import com.meclist.usecase.item.ListarItensPorCategoriaUseCase;
import com.meclist.usecase.item.ListarTodosItensUseCase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/itens")
public class ItemController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ListarItensPorCategoriaUseCase listarItensPorCategoriaUseCase;
    private final ListarTodosItensUseCase listarTodosItensUseCase;
    private final CadastrarItemUseCase cadastrarItemUseCase;
    private final AlterarSituacaoDoItemUseCase alterarSituacaoDoItemUseCase;
    private final AtualizarItemUseCase atualizarItemUseCase;

    public ItemController(
            ListarItensPorCategoriaUseCase listarItensPorCategoriaUseCase,
            ListarTodosItensUseCase listarTodosItensUseCase,
            CadastrarItemUseCase cadastrarItemUseCase,
            AlterarSituacaoDoItemUseCase alterarSituacaoDoItemUseCase,
            AtualizarItemUseCase atualizarItemUseCase) {

        this.listarItensPorCategoriaUseCase = listarItensPorCategoriaUseCase;
        this.listarTodosItensUseCase = listarTodosItensUseCase;
        this.cadastrarItemUseCase = cadastrarItemUseCase;
        this.alterarSituacaoDoItemUseCase = alterarSituacaoDoItemUseCase;
        this.atualizarItemUseCase = atualizarItemUseCase;
    }

    /**
     * Lista itens com filtros opcionais de categoria e situação.
     *
     * Se nenhuma categoria for informada, filtra apenas por situação.
     * Se nenhuma situação for informada, assume ATIVO como padrão.
     *
     * @param categoria Categoria opcional para filtrar os itens (ex: MOTOR,
     *                  SUSPENSAO)
     * @param situacao  Situação opcional (ATIVO ou INATIVO). Default: ATIVO
     * @return Lista de itens encontrados
     *
     *         Exemplos:
     *         GET /itens
     *         → Retorna itens ATIVOS de todas as categorias
     *
     *         GET /itens?situacao=INATIVO
     *         → Retorna itens INATIVOS de todas as categorias
     *
     *         GET /itens?categoria=DENTRO_DO_VEICULO
     *         → Retorna itens ATIVOS da categoria DENTRO_DO_VEICULO
     *
     *         GET /itens?categoria=DENTRO_DO_VEICULO&situacao=INATIVO
     *         → Retorna itens INATIVOS da categoria DENTRO_DO_VEICULO
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemResponse>>> listarItens(
            @RequestParam(required = false) CategoriaParteVeiculo categoria,
            @RequestParam(defaultValue = "ATIVO") Situacao situacao,
            HttpServletRequest request) {

        log.debug("Listando itens: categoria={}, situacao={}",
                categoria != null ? categoria : "TODOS",
                situacao);

        List<ItemResponse> itens = categoria != null
                ? listarItensPorCategoriaUseCase.executar(categoria, situacao)
                : listarTodosItensUseCase.executar(situacao);

        log.info("Itens listados: quantidade={}, categoria={}, situacao={}",
                itens.size(),
                categoria != null ? categoria : "TODOS",
                situacao);

        return success("Itens listados com sucesso!", itens, request);
    }

    /**
     * Cadastra um novo item no checklist com sua imagem ilustrativa.
     * 
     * @param nome           Nome descritivo do item
     * @param parteDoVeiculo Categoria/parte do veículo
     * @param imagem         Arquivo de imagem ilustrativa
     * @param force          Permite cadastrar mesmo se já existir item com mesmo
     *                       nome e categoria
     * @param request        HttpServletRequest para construir URI
     * @return ItemResponse com dados do item cadastrado
     * 
     *         Exemplo de uso (form-data):
     *         POST /itens
     *         nome: "Filtro de óleo"
     *         parteDoVeiculo: MOTOR
     *         imagem: [arquivo]
     * 
     *         POST /itens?force=true (para forçar cadastro de duplicata)
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ItemResponse>> cadastrarItem(
            @RequestParam @NotBlank(message = "Nome é obrigatório") String nome,
            @RequestParam @NotNull(message = "Parte do veículo é obrigatória") CategoriaParteVeiculo parteDoVeiculo,
            @RequestPart("imagem") @NotNull(message = "Imagem é obrigatória") MultipartFile imagem,
            HttpServletRequest request) {

        log.debug("Cadastrando novo item: nome={}, categoria={}, tamanhoImagem={}bytes",
                nome, parteDoVeiculo, imagem.getSize());

        CadastrarItemRequest cadastroRequest = new CadastrarItemRequest(nome, parteDoVeiculo);
        ItemResponse itemResponse = cadastrarItemUseCase.executar(cadastroRequest, imagem);

        log.info("Item cadastrado com sucesso: nome={}", nome);

        return created("Item cadastrado com sucesso!", itemResponse, request);
    }

    /**
     * Desativa (soft delete) um item.
     *
     * O registro continua existindo, mas passa a ter situação INATIVO
     * e não aparece nas listagens padrão (que filtram ATIVO).
     */
    @PatchMapping("/{idItem}/desativar")
    public ResponseEntity<ApiResponse<Void>> desativarItem(
            @PathVariable Long idItem,
            HttpServletRequest request) {

        alterarSituacaoDoItemUseCase.desativar(idItem);
        log.info("Item desativado com sucesso: idItem={}", idItem);

        return noContent();
    }

    /**
     * Reativa um item previamente desativado (situação volta para ATIVO).
     */
    @PatchMapping("/{idItem}/ativar")
    public ResponseEntity<ApiResponse<Void>> ativarItem(
            @PathVariable Long idItem,
            HttpServletRequest request) {

        alterarSituacaoDoItemUseCase.ativar(idItem);
        log.info("Item ativado com sucesso: idItem={}", idItem);

        return noContent();
    }

    /**
     * Atualiza um item existente do checklist.
     * 
     * Permite atualizar o nome, categoria e imagem ilustrativa do item.
     * Se nenhuma imagem for fornecida, mantém a imagem atual.
     * Se uma nova imagem for fornecida, deleta a antiga e faz upload da nova.
     * 
     * @param idItem         ID do item a ser atualizado
     * @param nome           Novo nome do item
     * @param parteDoVeiculo Nova categoria/parte do veículo
     * @param imagem         Novo arquivo de imagem ilustrativa (opcional)
     * @param servletRequest HttpServletRequest para logs
     * @return ItemResponse com dados do item atualizado
     * 
     *         Exemplo de uso (form-data):
     *         PUT /itens/123
     *         nome: "Filtro de ar"
     *         parteDoVeiculo: MOTOR
     *         imagem: [arquivo] (opcional)
     */
    @PutMapping(value = "/{idItem}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ItemResponse>> atualizarItem(
            @PathVariable @NotNull(message = "ID do item é obrigatório") Long idItem,
            @RequestParam @NotBlank(message = "Nome é obrigatório") String nome,
            @RequestParam @NotNull(message = "Parte do veículo é obrigatória") CategoriaParteVeiculo parteDoVeiculo,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem,
            HttpServletRequest servletRequest) {

        log.debug("Atualizando item: id={}, nome={}, categoria={}, temImagem={}",
                idItem, nome, parteDoVeiculo, imagem != null && !imagem.isEmpty());

        AtualizarItemRequest atualizarRequest = new AtualizarItemRequest(nome, parteDoVeiculo);
        ItemResponse itemResponse = atualizarItemUseCase.executar(idItem, atualizarRequest, imagem);

        log.info("Item atualizado com sucesso: id={}, nome={}", idItem, nome);

        return success("Item atualizado com sucesso!", itemResponse, servletRequest);
    }

}