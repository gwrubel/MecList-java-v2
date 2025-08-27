package com.meclist.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Checklist {
    private Long id;
    private Veiculo veiculo;
    private Mecanico mecanico;
    private Float quilometragem;
    private String descricao;
    private Status status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    
    // Lista de itens do checklist
    private List<ItemChecklist> itensChecklist = new ArrayList<>();
    
    // Lista de serviços
    private List<Servico> servicos = new ArrayList<>();
    
    // Lista de orçamentos
    private List<Orcamento> orcamentos = new ArrayList<>();

    // Construtor original mantido para compatibilidade
    public Checklist(Long id, Long idVeiculo, Long idMecanico, Float quilometragem, String descricao,
                     Long idStatus, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.quilometragem = quilometragem;
        this.descricao = descricao;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        // Os IDs serão convertidos para objetos posteriormente pelo mapper
    }

    // Novo construtor com objetos de domínio
    public Checklist(Long id, Veiculo veiculo, Mecanico mecanico, Float quilometragem, String descricao,
                     Status status, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.veiculo = veiculo;
        this.mecanico = mecanico;
        this.quilometragem = quilometragem;
        this.descricao = descricao;
        this.status = status;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    // Construtor com todas as listas
    public Checklist(Long id, Veiculo veiculo, Mecanico mecanico, Float quilometragem, String descricao,
                     Status status, List<ItemChecklist> itensChecklist, List<Servico> servicos, 
                     List<Orcamento> orcamentos, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.veiculo = veiculo;
        this.mecanico = mecanico;
        this.quilometragem = quilometragem;
        this.descricao = descricao;
        this.status = status;
        this.itensChecklist = itensChecklist != null ? itensChecklist : new ArrayList<>();
        this.servicos = servicos != null ? servicos : new ArrayList<>();
        this.orcamentos = orcamentos != null ? orcamentos : new ArrayList<>();
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Checklist novo(Long idVeiculo, Long idMecanico, Float quilometragem, String descricao, Long idStatus) {
        LocalDateTime agora = LocalDateTime.now();
        return new Checklist(null, idVeiculo, idMecanico, quilometragem, descricao, idStatus, agora, agora);
    }

    // Métodos para gerenciar itens do checklist
    public void adicionarItem(ItemChecklist item) {
        if (this.itensChecklist == null) {
            this.itensChecklist = new ArrayList<>();
        }
        this.itensChecklist.add(item);
    }

    public void removerItem(ItemChecklist item) {
        if (this.itensChecklist != null) {
            this.itensChecklist.remove(item);
        }
    }

    // Métodos para gerenciar serviços
    public void adicionarServico(Servico servico) {
        if (this.servicos == null) {
            this.servicos = new ArrayList<>();
        }
        this.servicos.add(servico);
    }

    public void removerServico(Servico servico) {
        if (this.servicos != null) {
            this.servicos.remove(servico);
        }
    }

    // Métodos para gerenciar orçamentos
    public void adicionarOrcamento(Orcamento orcamento) {
        if (this.orcamentos == null) {
            this.orcamentos = new ArrayList<>();
        }
        this.orcamentos.add(orcamento);
    }

    public void removerOrcamento(Orcamento orcamento) {
        if (this.orcamentos != null) {
            this.orcamentos.remove(orcamento);
        }
    }

    // Getters originais (mantidos para compatibilidade)
    public Long getId() { return id; }
    public Float getQuilometragem() { return quilometragem; }
    public String getDescricao() { return descricao; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    // Novos getters para objetos de domínio
    public Veiculo getVeiculo() { return veiculo; }
    public Mecanico getMecanico() { return mecanico; }
    public Status getStatus() { return status; }
    public List<ItemChecklist> getItensChecklist() { 
        return itensChecklist != null ? itensChecklist : new ArrayList<>(); 
    }
    public List<Servico> getServicos() { 
        return servicos != null ? servicos : new ArrayList<>(); 
    }
    public List<Orcamento> getOrcamentos() { 
        return orcamentos != null ? orcamentos : new ArrayList<>(); 
    }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }
    public void setMecanico(Mecanico mecanico) { this.mecanico = mecanico; }
    public void setQuilometragem(Float quilometragem) { this.quilometragem = quilometragem; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setStatus(Status status) { this.status = status; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
    public void setItensChecklist(List<ItemChecklist> itensChecklist) { 
        this.itensChecklist = itensChecklist != null ? itensChecklist : new ArrayList<>(); 
    }
    public void setServicos(List<Servico> servicos) { 
        this.servicos = servicos != null ? servicos : new ArrayList<>(); 
    }
    public void setOrcamentos(List<Orcamento> orcamentos) { 
        this.orcamentos = orcamentos != null ? orcamentos : new ArrayList<>(); 
    }

    // Métodos de compatibilidade (para manter funcionalidade existente)
    public Long getIdVeiculo() { 
        return veiculo != null ? veiculo.getId() : null; 
    }
    
    public Long getIdMecanico() { 
        return mecanico != null ? mecanico.getId() : null; 
    }
    
    public Long getIdStatus() { 
        return status != null ? status.getId() : null; 
    }
}



