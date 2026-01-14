package com.meclist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.dto.item.AtualizarItemRequest;
import com.meclist.dto.item.CadastrarItemRequest;
import com.meclist.dto.item.ItemResponse;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.item.AtualizarItemUseCase;
import com.meclist.usecase.item.CadastrarItemUseCase;
import com.meclist.usecase.item.ExcluirItemUseCase;
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
    private final ExcluirItemUseCase excluirItemUseCase;
    private final AtualizarItemUseCase atualizarItemUseCase;

    public ItemController(
            ListarItensPorCategoriaUseCase listarItensPorCategoriaUseCase,
            ListarTodosItensUseCase listarTodosItensUseCase,
            CadastrarItemUseCase cadastrarItemUseCase,
            ExcluirItemUseCase excluirItemUseCase,
            AtualizarItemUseCase atualizarItemUseCase) {
        
        this.listarItensPorCategoriaUseCase = listarItensPorCategoriaUseCase;
        this.listarTodosItensUseCase = listarTodosItensUseCase;
        this.cadastrarItemUseCase = cadastrarItemUseCase;
        this.excluirItemUseCase = excluirItemUseCase;
        this.atualizarItemUseCase = atualizarItemUseCase;
    }

    /**
     * Lista todos os itens do checklist, com opção de filtro por categoria.
     * 
     * Se nenhuma categoria for informada, retorna todos os itens.
     * Se uma categoria for fornecida, retorna apenas os itens dessa categoria.
     * 
     * @param categoria Categoria opcional para filtrar os itens (ex: MOTOR, SUSPENSAO)
     * @return Lista de itens encontrados
     * 
     * Exemplos:
     * GET /itens                          → Retorna todos os itens
     * GET /itens?categoria=DENTRO_DO_VEICULO          → Retorna apenas itens da categoria DENTRO_DO_VEICULO
     */
   @GetMapping
    public ResponseEntity<ApiResponse<List<ItemResponse>>> listarItens(
            @RequestParam(required = false) CategoriaParteVeiculo categoria,
            HttpServletRequest request) {
        
        log.debug("Listando itens: categoria={}", categoria != null ? categoria : "TODOS");
        
        List<ItemResponse> itens = categoria != null 
            ? listarItensPorCategoriaUseCase.executar(categoria)
            : listarTodosItensUseCase.executar();
        
        log.info("Itens listados: quantidade={}, categoria={}", 
                 itens.size(), categoria != null ? categoria : "TODOS");
        
        return success("Itens listados com sucesso!", itens, request);
    }

    /**
     * Cadastra um novo item no checklist com sua imagem ilustrativa.
     * 
     * @param nome Nome descritivo do item
     * @param parteDoVeiculo Categoria/parte do veículo
     * @param imagem Arquivo de imagem ilustrativa
     * @param force Permite cadastrar mesmo se já existir item com mesmo nome e categoria
     * @param request HttpServletRequest para construir URI
     * @return ItemResponse com dados do item cadastrado
     * 
     * Exemplo de uso (form-data):
     * POST /itens
     * nome: "Filtro de óleo"
     * parteDoVeiculo: MOTOR
     * imagem: [arquivo]
     * 
     * POST /itens?force=true  (para forçar cadastro de duplicata)
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ItemResponse>> cadastrarItem(
            @RequestParam @NotBlank(message = "Nome é obrigatório") String nome,
            @RequestParam @NotNull(message = "Parte do veículo é obrigatória") CategoriaParteVeiculo parteDoVeiculo,
            @RequestPart("imagem") @NotNull(message = "Imagem é obrigatória") MultipartFile imagem,
            @RequestParam(required = false, defaultValue = "false") boolean force,
            HttpServletRequest request) {

        log.debug("Cadastrando novo item: nome={}, categoria={}, force={}, tamanhoImagem={}bytes", 
                  nome, parteDoVeiculo, force, imagem.getSize());

        CadastrarItemRequest cadastroRequest = new CadastrarItemRequest(nome, parteDoVeiculo);
        ItemResponse itemResponse = cadastrarItemUseCase.executar(cadastroRequest, imagem, force);

        log.info("Item cadastrado com sucesso: nome={}, force={}", nome, force);

        return created("Item cadastrado com sucesso!", itemResponse, request);
    }

    /**
     * Exclui um item do checklist pelo seu ID.
     * 
     * Também remove a imagem ilustrativa associada ao item do sistema de arquivos.
     * 
     * @param idItem ID do item a ser excluído
     * @param request HttpServletRequest para logs e auditoria
     * @return Resposta sem conteúdo (204 No Content)
     * 
     * Exemplo de uso:
     * DELETE /itens/123
     */
    @DeleteMapping("/{idItem}")
    public ResponseEntity<ApiResponse<Void>> deletarItem(
            @PathVariable Long idItem,
            HttpServletRequest request) {
        
        
        excluirItemUseCase.executar(idItem);
        
        log.info("Item deletado com sucesso: idItem={}", idItem);
        
        return noContent();
    }

   

    /**
     * Atualiza um item existente do checklist.
     * 
     * Permite atualizar o nome, categoria e imagem ilustrativa do item.
     * Se nenhuma imagem for fornecida, mantém a imagem atual.
     * Se uma nova imagem for fornecida, deleta a antiga e faz upload da nova.
     * 
     * @param idItem ID do item a ser atualizado
     * @param nome Novo nome do item
     * @param parteDoVeiculo Nova categoria/parte do veículo
     * @param imagem Novo arquivo de imagem ilustrativa (opcional)
     * @param servletRequest HttpServletRequest para logs
     * @return ItemResponse com dados do item atualizado
     * 
     * Exemplo de uso (form-data):
     * PUT /itens/123
     * nome: "Filtro de ar"
     * parteDoVeiculo: MOTOR
     * imagem: [arquivo] (opcional)
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