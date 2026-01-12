package com.meclist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meclist.domain.ItemProduto;
import com.meclist.domain.Produto;
import com.meclist.dto.itemProduto.ProdutosDoItem;
import com.meclist.dto.produto.ProdutoRequest;
import com.meclist.response.ApiResponse;
import com.meclist.usecase.itemProduto.AtualizarNomeDoProdutoUseCase;
import com.meclist.usecase.itemProduto.CadastrarProdutoNoItemUseCase;
import com.meclist.usecase.itemProduto.ExcluirProdutoDoItemUseCase;
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
    private final ExcluirProdutoDoItemUseCase excluirProdutoDoItemUseCase;

    public ItemProdutoController(
            CadastrarProdutoNoItemUseCase cadastrarProdutoNoItemUseCase,
            ListarProdutosPorItemUseCase listarProdutosPorItemUseCase,
            AtualizarNomeDoProdutoUseCase atualizarNomeDoProdutoUseCase,
            ExcluirProdutoDoItemUseCase excluirProdutoDoItemUseCase) {
        
        this.cadastrarProdutoNoItemUseCase = cadastrarProdutoNoItemUseCase;
        this.listarProdutosPorItemUseCase = listarProdutosPorItemUseCase;
        this.atualizarNomeDoProdutoUseCase = atualizarNomeDoProdutoUseCase;
        this.excluirProdutoDoItemUseCase = excluirProdutoDoItemUseCase;
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
    public ResponseEntity<ApiResponse<ItemProduto>> cadastrarProduto(
            @PathVariable Long idItem,
            @RequestBody @Valid ProdutoRequest request,
            HttpServletRequest servletRequest) {
        
        log.debug("Cadastrando produto no item: idItem={}, nomeProduto={}", 
                  idItem, request.nomeProduto());
        
        ItemProduto itemProduto = cadastrarProdutoNoItemUseCase.executar(idItem, request);
        
        log.info("Produto cadastrado no item com sucesso: idItem={}, idProduto={}, nomeProduto={}", 
                 idItem, itemProduto.getProduto().getId(), request.nomeProduto());
        
        return created(
            "Produto cadastrado e associado ao item com sucesso!", 
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
    public ResponseEntity<ApiResponse<List<ProdutosDoItem>>> listarProdutos(
            @PathVariable Long idItem,
            HttpServletRequest servletRequest) {
        
        log.debug("Listando produtos do item: idItem={}", idItem);
        
        List<ProdutosDoItem> produtos = listarProdutosPorItemUseCase.executar(idItem);
        
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
    @PutMapping("/{idProduto}")
    public ResponseEntity<ApiResponse<Produto>> atualizarProduto(
            
            @PathVariable Long idProduto,
            @RequestBody @Valid ProdutoRequest request,
            HttpServletRequest servletRequest) {
        
        log.debug("Atualizando produto: idProduto={}, novoNome={}", 
                   idProduto, request.nomeProduto());
        
        Produto produto = atualizarNomeDoProdutoUseCase.executar(idProduto, request);
        
        
        return success(
            "Nome do produto atualizado com sucesso!", 
            produto, 
            servletRequest
        );
    }

    /**
     * Remove a associação de um produto com um item específico.
     * 
     * Este endpoint remove o produto do catálogo do item. O produto em si
     * não é deletado do sistema, apenas sua associação com este item específico.
     * 
     * 
     * Exemplo: Remover "Óleo 10W40" do item "Troca de óleo" se a oficina
     * não trabalha mais com este tipo de óleo.
     * 
     * @param idItem ID do item de onde o produto será removido
     * @param idProduto ID do produto a ser dissociado
     * @param servletRequest HttpServletRequest para logs
     * @return Resposta 204 No Content em caso de sucesso
     * 
     * Exemplo de uso:
     * DELETE /itens/1/produtos/5
     */
    @DeleteMapping("/{idProduto}")
    public ResponseEntity<ApiResponse<Void>> excluirProduto(
            @PathVariable Long idItem,
            @PathVariable Long idProduto,
            HttpServletRequest servletRequest) {
        
        log.debug("Removendo produto do item: idItem={}, idProduto={}", 
                  idItem, idProduto);
        
        excluirProdutoDoItemUseCase.executar(idItem, idProduto);
        
        log.info("Produto removido do item com sucesso: idItem={}, idProduto={}", 
                 idItem, idProduto);
        
        return noContent();
    }
}
