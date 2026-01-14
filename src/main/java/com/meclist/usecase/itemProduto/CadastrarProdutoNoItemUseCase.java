package com.meclist.usecase.itemProduto;

import org.springframework.stereotype.Service;
import com.meclist.interfaces.ProdutoGateway;
import com.meclist.mapper.ItemProdutoMapper;
import com.meclist.domain.Produto;
import com.meclist.dto.itemProduto.ItemProdutoResponse;
import com.meclist.dto.produto.ProdutoRequest;
import com.meclist.exception.ItemNaoEncontradoException;
import com.meclist.exception.ProdutoJaExisteException;
import com.meclist.interfaces.ItemGateway;
import com.meclist.interfaces.ItemProdutoGateway;
import com.meclist.domain.ItemProduto;
import com.meclist.domain.Item;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Service
public class CadastrarProdutoNoItemUseCase {
    private final ItemGateway itemGateway;
    private final ProdutoGateway produtoGateway;
    private final ItemProdutoGateway itemProdutoGateway;
    
    public CadastrarProdutoNoItemUseCase(ItemGateway itemGateway, ProdutoGateway produtoGateway, ItemProdutoGateway itemProdutoGateway) {
        this.itemGateway = itemGateway;
        this.produtoGateway = produtoGateway;
        this.itemProdutoGateway = itemProdutoGateway;
    }

    @Transactional
    public ItemProdutoResponse executar(Long idItem, ProdutoRequest request) {
        // 1. Buscar Item (validar se existe)
        Item item = itemGateway.buscarPorId(idItem)
            .orElseThrow(() -> new ItemNaoEncontradoException("Item não encontrado"));
        
        // 2. Buscar ou criar Produto
        Produto produto = buscarOuCriarProduto(request.nomeProduto());
        
        // 3. Verificar se já existe associação
        if (itemProdutoGateway.existeRelacionamento(item.getId(), produto.getId())) {
            throw new ProdutoJaExisteException("Produto já está associado a este item");
        }
        
        // 4. Criar associação ItemProduto
        ItemProduto itemProduto = ItemProduto.novo(item, produto);
        
        // 5. Salvar associação
        var itemProdutoSalvo = itemProdutoGateway.salvar(itemProduto);
        return ItemProdutoMapper.toResponse(itemProdutoSalvo);
    }

    private Produto buscarOuCriarProduto(String nomeProduto) {
        // Tenta buscar pelo nome
        Optional<Produto> produtoExistente = produtoGateway.buscarPorNome(nomeProduto.trim());
        
        if (produtoExistente.isPresent()) {
            return produtoExistente.get();
        }
        
        // Se não existe, cria novo
        Produto novoProduto = Produto.novo(nomeProduto.trim());
        return produtoGateway.salvar(novoProduto);
    }
}
