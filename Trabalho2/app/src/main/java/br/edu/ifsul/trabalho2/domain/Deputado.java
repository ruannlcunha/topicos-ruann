package br.edu.ifsul.trabalho2.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Deputado implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("nome")
    @Expose
    private String nome;

    @SerializedName("siglaPartido")
    @Expose
    private String siglaPartido;

    @SerializedName("siglaUf")
    @Expose
    private String siglaUf;

    public Deputado(Long id, String nome, String siglaPartido, String siglaUf) {
        this.id = id;
        this.nome = nome;
        this.siglaPartido = siglaPartido;
        this.siglaUf = siglaUf;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSiglaPartido() {
        return siglaPartido;
    }

    public void setSiglaPartido(String siglaPartido) {
        this.siglaPartido = siglaPartido;
    }

    public String getSiglaUf() {
        return siglaUf;
    }

    public void setSiglaUf(String siglaUf) {
        this.siglaUf = siglaUf;
    }
}
