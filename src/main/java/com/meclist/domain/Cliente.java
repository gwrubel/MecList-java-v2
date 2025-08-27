package com.meclist.domain;

import com.meclist.domain.enums.Situacao;
import com.meclist.domain.enums.TipoDeUsuario;
import com.meclist.domain.enums.TipoDocumento;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Cliente extends Usuario {

    private String documento;
    private TipoDocumento tipoDocumento;
    private String telefone;
    private String endereco;
    private List<Veiculo> veiculos = new ArrayList<>();

    public Cliente(Long id, String nome, String email, String senhaComHash, String documento, TipoDocumento tipoDocumento, String telefone, String endereco) {
        super(id, nome, email, senhaComHash, TipoDeUsuario.CLIENTE, Situacao.ATIVO);
        this.documento = documento;
        this.tipoDocumento = tipoDocumento;
        this.telefone = telefone;
        this.endereco = endereco;
    }
    

    // Construtor para reconstrução do Cliente a partir do banco
   public Cliente(
    Long id,
    String nome,
    String email,
    String senhaComHash,
    String documento,
    TipoDocumento tipoDocumento,
    String telefone,
    String endereco,
    Situacao situacao,
    List<Veiculo> veiculos,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {
    super(id, nome, email, senhaComHash, TipoDeUsuario.CLIENTE, criadoEm, atualizadoEm, situacao);
    this.documento = documento;
    this.tipoDocumento = tipoDocumento;
    this.telefone = telefone;
    this.endereco = endereco;
    this.veiculos = veiculos;
}


    // Método de fábrica para novo cadastro
    public static Cliente novoCadastro(String documento, TipoDocumento tipoDocumento, String telefone, String nome, String email, String senhaHash, String endereco) {
        return new Cliente(null, nome, email, senhaHash, documento, tipoDocumento, telefone, endereco);
    }

    
    public Cliente() {
        super(); 
    }
    public static Cliente somenteComId(Long id) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        return cliente;
    }
    
    
    
    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }
    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void adicionarVeiculo(Veiculo veiculo) {
        this.veiculos.add(veiculo);
    }

    public void removerVeiculo(Veiculo veiculo) {
        this.veiculos.remove(veiculo);
    }

    public String getDocumento() {
        return documento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void atualizarTelefone(String novoTelefone) {
        this.telefone = novoTelefone;
        atualizarUltimaAtualizacao();
    }

    
    public void atualizarDocumento(String documento, TipoDocumento tipoDocumento) {
        this.documento = documento;
        this.tipoDocumento = tipoDocumento;
        atualizarUltimaAtualizacao();
    }

    public void atualizarEndereco(String endereco) {
        this.endereco = endereco;
        atualizarUltimaAtualizacao();
    }

    
}

