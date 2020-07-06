package pet.yoko.apps.covid.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class EvolucaoTotalItem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "cidade")
    String cidade;

    @ColumnInfo(name = "data")
    String data;

    @ColumnInfo(name = "confirmados")
    int confirmados;

    @ColumnInfo(name = "obitos")
    int obitos;

    public EvolucaoTotalItem(String cidade, String data, int confirmados, int obitos) {
        this.cidade = cidade;
        this.data = data;
        this.confirmados = confirmados;
        this.obitos = obitos;
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
}
