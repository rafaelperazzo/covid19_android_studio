package pet.yoko.apps.covid.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class BairroMapaItem implements Serializable {

    public BairroMapaItem(String cidade, String bairro, int confirmados, double latitude, double longitude) {
        this.cidade = cidade;
        this.bairro = bairro;
        this.confirmados = confirmados;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "cidade")
    String cidade;

    @ColumnInfo(name = "bairro")
    String bairro;

    @ColumnInfo(name = "confirmados")
    int confirmados;

    @ColumnInfo(name = "latitude")
    double latitude;

    @ColumnInfo(name = "longitude")
    double longitude;

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

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public int getConfirmados() {
        return confirmados;
    }

    public void setConfirmados(int confirmados) {
        this.confirmados = confirmados;
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
}
