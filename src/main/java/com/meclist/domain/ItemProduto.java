package com.meclist.domain;

import java.time.LocalDateTime;

/**
 * ItemProduto representa o relacionamento N:N entre Item e Produto.
 * Serve como template/sugestão configurada pelo Admin.
 * Exemplo: "Sempre que houver 'Revisão de Freio', sugerir 'Pastilha'"
 */
public class ItemProduto {
    private Long id;
    private Item item;
    private Produto produto;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public ItemProduto(Long id, Item item, Produto produto, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.item = item;
        this.produto = produto;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static ItemProduto novo(Item item, Produto produto) {
        LocalDateTime agora = LocalDateTime.now();
        return new ItemProduto(null, item, produto, agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public Item getItem() { return item; }
    public Produto getProduto() { return produto; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setItem(Item item) { this.item = item; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}

