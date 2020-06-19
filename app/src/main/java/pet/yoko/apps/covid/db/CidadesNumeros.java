package pet.yoko.apps.covid.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class CidadesNumeros implements Serializable {

    public CidadesNumeros(String cidade, int confirmados, int suspeitos, int obitos, int recuperados, double taxa, int emRecuperacao) {
        this.cidade = cidade;
        this.confirmados = confirmados;
        this.suspeitos = suspeitos;
        this.obitos = obitos;
        this.recuperados = recuperados;
        this.taxa = taxa;
        this.emRecuperacao = emRecuperacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getConfirmados() {
        return confirmados;
    }

    public void setConfirmados(int confirmados) {
        this.confirmados = confirmados;
    }

    public int getSuspeitos() {
        return suspeitos;
    }

    public void setSuspeitos(int suspeitos) {
        this.suspeitos = suspeitos;
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

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "cidade")
    private String cidade;

    @ColumnInfo(name = "confirmados")
    private int confirmados;

    @ColumnInfo(name = "suspeitos")
    private int suspeitos;

    @ColumnInfo(name = "obitos")
    private int obitos;

    @ColumnInfo(name = "recuperados")
    private int recuperados;

    public double getTaxa() {
        return taxa;
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }

    @ColumnInfo(name = "taxa")
    private double taxa;

    public int getEmRecuperacao() {
        return emRecuperacao;
    }

    public void setEmRecuperacao(int emRecuperacao) {
        this.emRecuperacao = emRecuperacao;
    }

    private int emRecuperacao;



}
