package com.meclist.domain;

import java.time.LocalDateTime;

public class FotoEvidencia {
    private Long id;
    private ItemChecklist itemChecklist;
    private String urlFoto;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public FotoEvidencia(Long id, ItemChecklist itemChecklist, String urlFoto, 
                        LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.itemChecklist = itemChecklist;
        this.urlFoto = urlFoto;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static FotoEvidencia nova(ItemChecklist itemChecklist, String urlFoto) {
        LocalDateTime agora = LocalDateTime.now();
        return new FotoEvidencia(null, itemChecklist, urlFoto, agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public ItemChecklist getItemChecklist() { return itemChecklist; }
    public String getUrlFoto() { return urlFoto; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setItemChecklist(ItemChecklist itemChecklist) { this.itemChecklist = itemChecklist; }
    public void setUrlFoto(String urlFoto) { this.urlFoto = urlFoto; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
