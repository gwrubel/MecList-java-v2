package com.meclist.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.meclist.domain.enums.StatusItem;

public class ItemChecklist {
    private Long id;
    private Checklist checklist;
    private Item item;
    private StatusItem statusItem;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    
    // Lista de fotos de evidência
    private List<FotoEvidencia> fotosEvidencia = new ArrayList<>();
    
    // Lista de produtos orçados (checklist_produto)
    private List<ChecklistProduto> produtosOrcados = new ArrayList<>();
    

    // Construtor completo
    public ItemChecklist(Long id, Checklist checklist, Item item, StatusItem statusItem,
                         LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.checklist = checklist;
        this.item = item;
        this.statusItem = statusItem != null ? statusItem : StatusItem.PENDENTE;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    // Factory method para criar novo item de checklist
    public static ItemChecklist novo(Checklist checklist, Item item) {
        LocalDateTime agora = LocalDateTime.now();
        return new ItemChecklist(null, checklist, item, StatusItem.PENDENTE, agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public Checklist getChecklist() { return checklist; }
    public Item getItem() { return item; }
    public StatusItem getStatusItem() { return statusItem; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public List<FotoEvidencia> getFotosEvidencia() { return fotosEvidencia; }
    public List<ChecklistProduto> getProdutosOrcados() { return produtosOrcados; }

    // Métodos de negócio
    public void atualizarStatus(StatusItem novoStatus) {
        this.statusItem = novoStatus;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void adicionarFotoEvidencia(FotoEvidencia foto) {
        if (this.fotosEvidencia == null) {
            this.fotosEvidencia = new ArrayList<>();
        }
        this.fotosEvidencia.add(foto);
        this.atualizadoEm = LocalDateTime.now();
    }

    public void adicionarFotosEvidencia(List<FotoEvidencia> fotos) {
        if (this.fotosEvidencia == null) {
            this.fotosEvidencia = new ArrayList<>();
        }
        this.fotosEvidencia.addAll(fotos);
        this.atualizadoEm = LocalDateTime.now();
    }

    public void limparFotosEvidencia() {
        if (this.fotosEvidencia != null) {
            this.fotosEvidencia.clear();
        }
        this.atualizadoEm = LocalDateTime.now();
    }

    public void adicionarProdutoOrcado(ChecklistProduto produto) {
        if (this.produtosOrcados == null) {
            this.produtosOrcados = new ArrayList<>();
        }
        this.produtosOrcados.add(produto);
        this.atualizadoEm = LocalDateTime.now();
    }

    public void adicionarProdutosOrcados(List<ChecklistProduto> produtos) {
        if (this.produtosOrcados == null) {
            this.produtosOrcados = new ArrayList<>();
        }
        this.produtosOrcados.addAll(produtos);
        this.atualizadoEm = LocalDateTime.now();
    }

    public void limparProdutosOrcados() {
        if (this.produtosOrcados != null) {
            this.produtosOrcados.clear();
        }
        this.atualizadoEm = LocalDateTime.now();
    }

    public void setAtualizadoEm(LocalDateTime dataAtualizacao) {
        this.atualizadoEm = dataAtualizacao;
    }
       
}



