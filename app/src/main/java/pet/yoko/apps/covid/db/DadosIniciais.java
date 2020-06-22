package pet.yoko.apps.covid.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class DadosIniciais implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "confirmados")
    int confirmados;

    @ColumnInfo(name = "obitos")
    int obitos;

    @ColumnInfo(name = "recuperados")
    int recuperados;

    @ColumnInfo(name = "emRecuperacao")
    int emRecuperacao;

    @ColumnInfo(name = "atualizacao")
    String atualizacao;

    @ColumnInfo(name = "taxa")
    double taxa;

    @ColumnInfo(name = "confirmacoes")
    int confirmacoes;

    @ColumnInfo(name = "comorbidades")
    String comorbidades;

    @ColumnInfo(name = "mediaObitosPorDia")
    double mediaObitosPorDia;

    @ColumnInfo(name = "medianaIdade")
    double medianaIdade;

    @ColumnInfo(name = "uti")
    int uti;

    @ColumnInfo(name = "enfermaria")
    int enfermaria;

    public String getComorbidades() {
        return comorbidades;
    }

    public void setComorbidades(String comorbidades) {
        this.comorbidades = comorbidades;
    }

    public double getMediaObitosPorDia() {
        return mediaObitosPorDia;
    }

    public void setMediaObitosPorDia(double mediaObitosPorDia) {
        this.mediaObitosPorDia = mediaObitosPorDia;
    }

    public double getMedianaIdade() {
        return medianaIdade;
    }

    public void setMedianaIdade(double medianaIdade) {
        this.medianaIdade = medianaIdade;
    }

    public int getUti() {
        return uti;
    }

    public void setUti(int uti) {
        this.uti = uti;
    }

    public int getEnfermaria() {
        return enfermaria;
    }

    public void setEnfermaria(int enfermaria) {
        this.enfermaria = enfermaria;
    }

    public int getConfirmacoes() {
        return confirmacoes;
    }

    public void setConfirmacoes(int confirmacoes) {
        this.confirmacoes = confirmacoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConfirmados() {
        return confirmados;
    }

    public void setConfirmados(int confirmados) {
        this.confirmados = confirmados;
    }

    public int getObitos() {
        return obitos;
    }

    public void setObitos(int obitos) {
        this.obitos = obitos;
    }

    public int getRecuperados() {
        return recuperados;
    }

    public void setRecuperados(int recuperados) {
        this.recuperados = recuperados;
    }

    public int getEmRecuperacao() {
        return emRecuperacao;
    }

    public void setEmRecuperacao(int emRecuperacao) {
        this.emRecuperacao = emRecuperacao;
    }

    public String getAtualizacao() {
        return atualizacao;
    }

    public void setAtualizacao(String atualizacao) {
        this.atualizacao = atualizacao;
    }

    public double getTaxa() {
        return taxa;
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }

    public DadosIniciais(int confirmados, int obitos, int recuperados, int emRecuperacao, String atualizacao, double taxa, int confirmacoes, String comorbidades, double mediaObitosPorDia, double medianaIdade, int uti, int enfermaria) {
        this.confirmados = confirmados;
        this.obitos = obitos;
        this.recuperados = recuperados;
        this.emRecuperacao = emRecuperacao;
        this.atualizacao = atualizacao;
        this.taxa = taxa;
        this.confirmacoes = confirmacoes;
        this.comorbidades = comorbidades;
        this.mediaObitosPorDia = mediaObitosPorDia;
        this.medianaIdade = medianaIdade;
        this.uti = uti;
        this.enfermaria = enfermaria;
    }
}
