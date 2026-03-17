package com.meclist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.meclist.dto.itemProduto.ItemProdutoResponse;
import com.meclist.dto.itemProduto.ProdutosDoItemResponse;
import com.meclist.dto.produto.ProdutoRequest;
import com.meclist.domain.enums.Situacao;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.itemProduto.AtualizarNomeDoProdutoUseCase;
import com.meclist.usecase.itemProduto.CadastrarProdutoNoItemUseCase;
import com.meclist.usecase.itemProduto.AlterarSituacaoDoProdutoPorItemUseCase;
import com.meclist.usecase.itemProduto.ListarProdutosPorItemUseCase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/itens/{idItem}/produtos")
public class ItemProdutoController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ItemProdutoController.class);

    private final CadastrarProdutoNoItemUseCase cadastrarProdutoNoItemUseCase;
    private final ListarProdutosPorItemUseCase listarProdutosPorItemUseCase;
    private final AtualizarNomeDoProdutoUseCase atualizarNomeDoProdutoUseCase;
    private final AlterarSituacaoDoProdutoPorItemUseCase alterarSituacaoDoProdutoPorItemUseCase;

    public ItemProdutoController(
            CadastrarProdutoNoItemUseCase cadastrarProdutoNoItemUseCase,
            ListarProdutosPorItemUseCase listarProdutosPorItemUseCase,
            AtualizarNomeDoProdutoUseCase atualizarNomeDoProdutoUseCase,
            AlterarSituacaoDoProdutoPorItemUseCase alterarSituacaoDoProdutoPorItemUseCase) {
        
        this.cadastrarProdutoNoItemUseCase = cadastrarProdutoNoItemUseCase;
        this.listarProdutosPorItemUseCase = listarProdutosPorItemUseCase;
        this.atualizarNomeDoProdutoUseCase = atualizarNomeDoProdutoUseCase;
        this.alterarSituacaoDoProdutoPorItemUseCase = alterarSituacaoDoProdutoPorItemUseCase;
    }

    /**
     * Cadastra um produto no catálogo de um item específico.
     * 
     * @param idItem ID do item do checklist que receberá o produto
     * @param request Dados do produto (apenas o nome é necessário)
     * @param servletRequest HttpServletRequest para construir URI do recurso
     * @return ItemProduto criado (associação entre item e produto)
     * 
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ItemProdutoResponse>> cadastrarProduto(
            @PathVariable Long idItem,
            @RequestBody @Valid ProdutoRequest request,
            HttpServletRequest servletRequest) {
        
        log.debug("Cadastrando produto no item: idItem={}, nomeProduto={}", 
                  idItem, request.nomeProduto());
        
        ItemProdutoResponse itemProduto = cadastrarProdutoNoItemUseCase.executar(idItem, request);
        
        log.info("Produto cadastrado no item com sucesso: idItem={}, produtoId={}, nomeProduto={}", 
                 idItem, itemProduto.produtoId(), request.nomeProduto());
        
        return created(
            "Produto cadastrado com sucesso!", 
            itemProduto, 
            servletRequest
        );
    }

    /**
     * Lista todos os produtos cadastrados no catálogo de um item específico.
     * 
     * Retorna todos os produtos que foram previamente associados ao item,
     * formando o catálogo de produtos disponíveis para aquele item do checklist.
     * 
     * Caso de uso:
     * O Administrador pode consultar quais produtos já estão cadastrados em um
     * item antes de adicionar novos. O Mecânico também poderá consultar esta
     * lista durante a inspeção para selecionar produtos a sugerir.
     * 
     * @param idItem ID do item cujos produtos serão listados
     * @param servletRequest HttpServletRequest para logs e auditoria
     * @return Lista de produtos associados ao item
     * 
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProdutosDoItemResponse>>> listarProdutos(
            @PathVariable Long idItem,
            @RequestParam(required = false) Situacao situacao,
            HttpServletRequest servletRequest) {
        
        log.debug("Listando produtos do item: idItem={}, situacao={}", idItem, situacao);
        
        List<ProdutosDoItemResponse> produtos = listarProdutosPorItemUseCase.executar(idItem, situacao);
        
        log.info("Produtos listados com sucesso: idItem={}, quantidade={}", 
                 idItem, produtos.size());
        
        return success(
            "Produtos listados com sucesso!", 
            produtos, 
            servletRequest
        );
    }

    /**
     * Atualiza as informações de um produto específico.
     * 
     * Permite ao Administrador corrigir ou modificar o nome de um produto
     * que já está cadastrado no catálogo de um item.
     * 
     * IMPORTANTE: Esta operação atualiza o produto globalmente. Se o mesmo
     * produto estiver associado a múltiplos itens, a alteração será refletida
     * em todos eles.
     * 
     * @param idItem ID do item (usado para validação de contexto)
     * @param idProduto ID do produto a ser atualizado
     * @param request Novos dados do produto
     * @param servletRequest HttpServletRequest para logs
     * @return Produto atualizado
     * 
     
     */
    @PutMapping("/{produtoId}")
    public ResponseEntity<ApiResponse<ItemProdutoResponse>> atualizarProduto(
            
            @PathVariable Long produtoId,
            @PathVariable Long idItem,
            @RequestBody @Valid ProdutoRequest request,
            HttpServletRequest servletRequest
        ) {
        
        log.debug("Atualizando produto: produtoId={}, novoNome={}", 
                   produtoId, request.nomeProduto());
        
        ItemProdutoResponse produto = atualizarNomeDoProdutoUseCase.executar(produtoId, request, idItem);
        
        
        return success(
            "Nome do produto atualizado com sucesso!", 
            produto, 
            servletRequest
        );
    }

   @PatchMapping("/{produtoId}/desativar")
    public ResponseEntity<ApiResponse<Void>> desativarProduto(
        @PathVariable Long produtoId,
        @PathVariable Long idItem,
        HttpServletRequest request
    ){
        alterarSituacaoDoProdutoPorItemUseCase.desativar(produtoId, idItem);
        log.info("Produto desativado com sucesso: produtoId={}, idItem={}", produtoId, idItem);

        return noContent();
    }

    @PatchMapping("/{produtoId}/ativar")
    public ResponseEntity<ApiResponse<Void>> ativarProduto(
        @PathVariable Long produtoId,
        @PathVariable Long idItem,
        HttpServletRequest request
    ){
        alterarSituacaoDoProdutoPorItemUseCase.ativar(produtoId, idItem);
        log.info("Produto ativado com sucesso: produtoId={}, idItem={}", produtoId, idItem);

        return noContent();
     }
 
}
