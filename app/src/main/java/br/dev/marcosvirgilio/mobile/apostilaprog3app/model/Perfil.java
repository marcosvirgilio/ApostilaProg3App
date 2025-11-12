package br.dev.marcosvirgilio.mobile.apostilaprog3app.model;

public class Perfil {
    private int idPerfil;
    private String nmPerfil;
    private String dePerfil;

    @Override
    public String toString() {return this.nmPerfil;}

    public int getIdPerfil() {return idPerfil;}

    public void setIdPerfil(int idPerfil) {this.idPerfil = idPerfil;}

    public String getNmPerfil() {return nmPerfil;}

    public void setNmPerfil(String nmPerfil) {this.nmPerfil = nmPerfil;}

    public String getDePerfil() {return dePerfil;}

    public void setDePerfil(String dePerfil) {this.dePerfil = dePerfil;}
}
