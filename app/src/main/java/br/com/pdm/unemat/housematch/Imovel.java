package br.com.pdm.unemat.housematch;

public class Imovel {
    private String titulo;
    private String descricao;
    private Float preco;
    private String tipo;
    private String nomeProprietario;
    private String numeroProprietario;

    public Imovel(String titulo, String descricao, Float preco, String tipo, String nomeProprietario, String numeroProprietario) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.tipo = tipo;
        this.nomeProprietario = nomeProprietario;
        this.numeroProprietario = numeroProprietario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNomeProprietario() {
        return nomeProprietario;
    }

    public void setNomeProprietario(String nomeProprietario) {
        this.nomeProprietario = nomeProprietario;
    }

    public String getNumeroProprietario() {
        return numeroProprietario;
    }

    public void setNumeroProprietario(String numeroProprietario) {
        this.numeroProprietario = numeroProprietario;
    }
}
