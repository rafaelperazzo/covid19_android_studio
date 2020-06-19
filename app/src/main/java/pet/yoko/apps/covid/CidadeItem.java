package pet.yoko.apps.covid;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class CidadeItem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "cidade")
    String cidade;
    @ColumnInfo(name = "confirmados")
    int confirmados;
    @ColumnInfo(name = "suspeitos")
    int suspeitos;
    @ColumnInfo(name = "obitos")
    int obitos;
    @ColumnInfo(name = "incidencia")
    double incidencia;
    @ColumnInfo(name = "recuperados")
    int recuperados;
    @ColumnInfo(name = "emRecuperacao")
    int emRecuperacao;

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

    public double getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(double incidencia) {
        this.incidencia = incidencia;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    /*public CidadeItem(String cidade, int confirmados, int suspeitos, int obitos,double incidencia) {
        this.cidade = cidade;
        this.confirmados = confirmados;
        this.suspeitos = suspeitos;
        this.obitos = obitos;
        this.incidencia = incidencia;
    }*/

    public CidadeItem(String cidade, int confirmados, int suspeitos, int obitos,double incidencia,int recuperados,int emRecuperacao) {
        this.cidade = cidade;
        this.confirmados = confirmados;
        this.suspeitos = suspeitos;
        this.obitos = obitos;
        this.incidencia = incidencia;
        this.recuperados=recuperados;
        this.emRecuperacao=emRecuperacao;
    }
}
