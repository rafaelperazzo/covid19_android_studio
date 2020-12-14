package pet.yoko.apps.covid.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class EvolucaoMediaItem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "cidade")
    String cidade;

    @ColumnInfo(name = "data")
    String data;

    @ColumnInfo(name = "confirmados")
    float confirmados;

    @ColumnInfo(name = "obitos")
    float obitos;

    @ColumnInfo(name = "situacao_confirmados")
    int situacao_confirmados;

    @ColumnInfo(name = "situacao_obitos")
    int situacao_obitos;

    public EvolucaoMediaItem(String cidade, String data, float confirmados, float obitos, int situacao_confirmados, int situacao_obitos) {
        this.cidade = cidade;
        this.data = data;
        this.confirmados = confirmados;
        this.obitos = obitos;
        this.situacao_confirmados = situacao_confirmados;
        this.situacao_obitos = situacao_obitos;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public float getConfirmados() {
        return confirmados;
    }

    public void setConfirmados(float confirmados) {
        this.confirmados = confirmados;
    }

    public float getObitos() {
        return obitos;
    }

    public void setObitos(float obitos) {
        this.obitos = obitos;
    }

    public int getSituacao_confirmados() {
        return situacao_confirmados;
    }

    public void setSituacao_confirmados(int situacao_confirmados) {
        this.situacao_confirmados = situacao_confirmados;
    }

    public int getSituacao_obitos() {
        return situacao_obitos;
    }

    public void setSituacao_obitos(int situacao_obitos) {
        this.situacao_obitos = situacao_obitos;
    }
}
