package pet.yoko.apps.covid.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class CidadeMapaItem implements Serializable {

    public CidadeMapaItem(String cidade, int confirmados, int emRecuperacao, int obitos, int recuperados, double latitude, double longitude, double incidencia) {
        this.cidade = cidade;
        this.confirmados = confirmados;
        this.emRecuperacao = emRecuperacao;
        this.obitos = obitos;
        this.recuperados = recuperados;
        this.latitude = latitude;
        this.longitude = longitude;
        this.incidencia = incidencia;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "cidade")
    String cidade;

    @ColumnInfo(name = "confirmados")
    int confirmados;

    @ColumnInfo(name = "emRecuperacao")
    int emRecuperacao;

    @ColumnInfo(name = "obitos")
    int obitos;

    @ColumnInfo(name = "recuperados")
    int recuperados;

    @ColumnInfo(name = "latitude")
    double latitude;

    @ColumnInfo(name = "longitude")
    double longitude;

    @ColumnInfo(name = "incidencia")
    double incidencia;

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

    public int getEmRecuperacao() {
        return emRecuperacao;
    }

    public void setEmRecuperacao(int emRecuperacao) {
        this.emRecuperacao = emRecuperacao;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(double incidencia) {
        this.incidencia = incidencia;
    }
}
