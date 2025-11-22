package com.meclist.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // Construtor original mantido para compatibilidade
    public ItemChecklist(Long id, Long idChecklist, Long idItem, Long idStatusItem,
                         LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        // Os IDs serão convertidos para objetos posteriormente pelo mapper
    }

    // Novo construtor com objetos de domínio
    public ItemChecklist(Long id, Checklist checklist, Item item, StatusItem statusItem,
                         LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.checklist = checklist;
        this.item = item;
        this.statusItem = statusItem;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    // Construtor com fotos e produtos orçados
    public ItemChecklist(Long id, Checklist checklist, Item item, StatusItem statusItem,
                         List<FotoEvidencia> fotosEvidencia, List<ChecklistProduto> produtosOrcados,
                         LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.checklist = checklist;
        this.item = item;
        this.statusItem = statusItem;
        this.fotosEvidencia = fotosEvidencia != null ? fotosEvidencia : new ArrayList<>();
        this.produtosOrcados = produtosOrcados != null ? produtosOrcados : new ArrayList<>();
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static ItemChecklist novo(Long idChecklist, Long idItem, Long idStatusItem) {
        LocalDateTime agora = LocalDateTime.now();
        return new ItemChecklist(null, idChecklist, idItem, idStatusItem, agora, agora);
    }

    // Métodos para gerenciar fotos de evidência
    public void adicionarFoto(FotoEvidencia foto) {
        if (this.fotosEvidencia == null) {
            this.fotosEvidencia = new ArrayList<>();
        }
        this.fotosEvidencia.add(foto);
    }

    public void removerFoto(FotoEvidencia foto) {
        if (this.fotosEvidencia != null) {
            this.fotosEvidencia.remove(foto);
        }
    }

    public void limparFotos() {
        if (this.fotosEvidencia != null) {
            this.fotosEvidencia.clear();
        }
    }

    // Métodos para gerenciar produtos orçados
    public void adicionarProdutoOrcado(ChecklistProduto produtoOrcado) {
        if (this.produtosOrcados == null) {
            this.produtosOrcados = new ArrayList<>();
        }
        this.produtosOrcados.add(produtoOrcado);
    }

    public void removerProdutoOrcado(ChecklistProduto produtoOrcado) {
        if (this.produtosOrcados != null) {
            this.produtosOrcados.remove(produtoOrcado);
        }
    }

    public void limparProdutosOrcados() {
        if (this.produtosOrcados != null) {
            this.produtosOrcados.clear();
        }
    }

    // Getters originais (mantidos para compatibilidade)
    public Long getId() { return id; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Novos getters para objetos de domínio
    public Checklist getChecklist() { return checklist; }
    public Item getItem() { return item; }
    public StatusItem getStatusItem() { return statusItem; }
    public List<FotoEvidencia> getFotosEvidencia() { 
        return fotosEvidencia != null ? fotosEvidencia : new ArrayList<>(); 
    }
    public List<ChecklistProduto> getProdutosOrcados() { 
        return produtosOrcados != null ? produtosOrcados : new ArrayList<>(); 
    }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setChecklist(Checklist checklist) { this.checklist = checklist; }
    public void setItem(Item item) { this.item = item; }
    public void setStatusItem(StatusItem statusItem) { this.statusItem = statusItem; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
    public void setFotosEvidencia(List<FotoEvidencia> fotosEvidencia) { 
        this.fotosEvidencia = fotosEvidencia != null ? fotosEvidencia : new ArrayList<>(); 
    }
    public void setProdutosOrcados(List<ChecklistProduto> produtosOrcados) { 
        this.produtosOrcados = produtosOrcados != null ? produtosOrcados : new ArrayList<>(); 
    }

    // Métodos de compatibilidade (para manter funcionalidade existente)
    public Long getIdChecklist() { 
        return checklist != null ? checklist.getId() : null; 
    }
    
    public Long getIdItem() { 
        return item != null ? item.getId() : null; 
    }
    
    public Long getIdStatusItem() { 
        return statusItem != null ? statusItem.getId() : null; 
    }
}



