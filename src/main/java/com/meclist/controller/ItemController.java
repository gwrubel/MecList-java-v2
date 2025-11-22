package com.meclist.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.meclist.domain.Item;
import com.meclist.domain.ItemProduto;
import com.meclist.domain.enums.CategoriaParteVeiculo;
import com.meclist.dto.checklist.ItemsPorCategoriaResponse;
import com.meclist.dto.item.CadastrarItemRequest;
import com.meclist.dto.itemProduto.ItemProdutoResponse;
import com.meclist.dto.produto.ProdutoRequest;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.item.CadastrarItemUseCase;
import com.meclist.usecase.item.ListarItensPorCategoriaUseCase;
import com.meclist.usecase.item.ListarItensAgrupadosUseCase; 
import com.meclist.usecase.itemProduto.CadastrarProdutoUseCase;
import com.meclist.usecase.itemProduto.ListarProdutosPorItemUseCase;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Controller para gerenciar Itens e Produtos associados.
 * 
 * Endpoints:
 * - GET    /itens              - Lista itens (com filtro opcional)
 * - GET    /itens/agrupados    - Lista itens agrupados por categoria
 * - POST   /itens              - Cadastra novo item com imagem
 * - POST   /itens/{id}/produtos - Cadastra produto em um item
 * - GET    /itens/{id}/produtos - Lista produtos de um item
 */
@RestController
@RequestMapping("/itens")
public class ItemController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ListarItensPorCategoriaUseCase listarItensPorCategoriaUseCase;
    private final ListarItensAgrupadosUseCase listarItensAgrupadosUseCase;  
    private final CadastrarItemUseCase cadastrarItemUseCase;
    private final CadastrarProdutoUseCase cadastrarProdutoUseCase;
    private final ListarProdutosPorItemUseCase listarProdutosPorItemUseCase;

    public ItemController(
            ListarItensPorCategoriaUseCase listarItensPorCategoriaUseCase,
            ListarItensAgrupadosUseCase listarItensAgrupadosUseCase,  
            CadastrarItemUseCase cadastrarItemUseCase,
            CadastrarProdutoUseCase cadastrarProdutoUseCase,
            ListarProdutosPorItemUseCase listarProdutosPorItemUseCase) {
        
        this.listarItensPorCategoriaUseCase = listarItensPorCategoriaUseCase;
        this.listarItensAgrupadosUseCase = listarItensAgrupadosUseCase; 
        this.cadastrarItemUseCase = cadastrarItemUseCase;
        this.cadastrarProdutoUseCase = cadastrarProdutoUseCase;
        this.listarProdutosPorItemUseCase = listarProdutosPorItemUseCase;
    }

    /**
     * Lista itens, opcionalmente filtrados por categoria.
     * 
     * @param categoria Categoria opcional para filtro
     * @return Lista de itens como responses
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<?>>> listarPorCategoria(
            @RequestParam(required = false) CategoriaParteVeiculo categoria) {
        
        List<?> itens = categoria != null 
            ? listarItensPorCategoriaUseCase.executar(categoria)
            : List.of();
        
        return ResponseEntity.ok(
            ApiResponse.success("Itens listados com sucesso", null, itens)
        );
    }

    /**
     * Lista todos os itens agrupados por categoria.
     * 
     * @return Itens agrupados por categoria
     */
    @GetMapping("/agrupados")
    public ResponseEntity<ApiResponse<List<ItemsPorCategoriaResponse>>> listarAgrupados() {
        List<ItemsPorCategoriaResponse> itensAgrupados = listarItensAgrupadosUseCase.executar();
        
        return ResponseEntity.ok(
            ApiResponse.success(
                "Itens listados e agrupados com sucesso", 
                null, 
                itensAgrupados
            )
        );
    }

    /**
     * Cadastra um novo item com imagem.
     * 
     * @param nome Nome do item
     * @param parteDoVeiculo Categoria/parte do veículo
     * @param imagem Arquivo de imagem
     * @param request HttpServletRequest para obter informações da requisição
     * @return Item criado
     */
@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ApiResponse<Item>> cadastrarItem(
        @RequestParam String nome,
        @RequestParam CategoriaParteVeiculo parteDoVeiculo,
        @RequestPart("imagem") MultipartFile imagem,
        HttpServletRequest request) throws IOException {

    log.debug("Recebido request para cadastrar item: nome={}, categoria={}", nome, parteDoVeiculo);

    CadastrarItemRequest cadastroRequest = new CadastrarItemRequest(nome, parteDoVeiculo);
    Item item = cadastrarItemUseCase.executar(cadastroRequest, imagem.getBytes());

    log.info("Item cadastrado com sucesso: id={}, nome={}", item.getId(), nome);

    return created("Item cadastrado com sucesso!", item, request);

}

    /**
     * Cadastra um produto associado a um item.
     * 
     * @param idItem ID do item
     * @param request Dados do produto a cadastrar
     * @param servletRequest HttpServletRequest
     * @return ItemProduto criado
     */
    @PostMapping("/{idItem}/produtos")
    public ResponseEntity<ApiResponse<ItemProduto>> cadastrarProdutoNoItem(
            @PathVariable Long idItem,
            @RequestBody @Valid ProdutoRequest request,
            HttpServletRequest servletRequest) {
        
        log.debug("Cadastrando produto no item: idItem={}, produtoNome={}", 
                  idItem, request.nomeProduto());
        
        ItemProduto itemProduto = cadastrarProdutoUseCase.executar(idItem, request);
        
        log.info("Produto cadastrado no item: idItem={}, idProduto={}", 
                 idItem, itemProduto.getId());
        
        return created(
            "Produto cadastrado e associado ao item com sucesso!", 
            itemProduto, 
            servletRequest
        );
    }

    /**
     * Lista todos os produtos associados a um item.
     * 
     * @param idItem ID do item
     * @param servletRequest HttpServletRequest
     * @return Lista de produtos
     */
    @GetMapping("/{idItem}/produtos")
    public ResponseEntity<ApiResponse<List<ItemProdutoResponse>>> listarProdutosPorItem(
            @PathVariable Long idItem,
            HttpServletRequest servletRequest) {
        
        log.debug("Listando produtos do item: idItem={}", idItem);
        
        List<ItemProdutoResponse> produtos = listarProdutosPorItemUseCase.executar(idItem);
        
        log.info("Produtos listados: idItem={}, quantidade={}", idItem, produtos.size());
        
        return success(
            "Produtos listados com sucesso!", 
            produtos, 
            servletRequest
        );
    }
}