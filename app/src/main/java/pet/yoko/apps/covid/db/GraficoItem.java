package pet.yoko.apps.covid.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class GraficoItem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "tipo")
    String tipo;

    @ColumnInfo(name = "label")
    String label;

    @ColumnInfo(name = "quantidade")
    int quantidade;

    @ColumnInfo(name = "quantidade2")
    int quantidade2;

    public GraficoItem(String tipo, String label, int quantidade, int quantidade2) {
        this.tipo = tipo;
        this.label = label;
        this.quantidade = quantidade;
        this.quantidade2 = quantidade2;
    }

    public int getQuantidade2() {
        return quantidade2;
    }

    public void setQuantidade2(int quantidade2) {
        this.quantidade2 = quantidade2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

}
