package com.meclist.domain;

import java.time.LocalDateTime;

public class FotoEvidencia {
    private Long id;
    private ItemChecklist itemChecklist;
    private String pathFoto;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public FotoEvidencia(Long id, ItemChecklist itemChecklist, String pathFoto, 
                        LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.itemChecklist = itemChecklist;
        this.pathFoto = pathFoto;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static FotoEvidencia nova(ItemChecklist itemChecklist, String pathFoto) {
        LocalDateTime agora = LocalDateTime.now();
        return new FotoEvidencia(null, itemChecklist, pathFoto, agora, agora);
    }

    // Getters
    public Long getId() { return id; }
    public ItemChecklist getItemChecklist() { return itemChecklist; }
    public String getPathFoto() { return pathFoto; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setItemChecklist(ItemChecklist itemChecklist) { this.itemChecklist = itemChecklist; }
    public void setPathFoto(String pathFoto) { this.pathFoto = pathFoto; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
