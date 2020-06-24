package pet.yoko.apps.covid.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class CidadaMapaItem implements Serializable {

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
    int latitude;

    @ColumnInfo(name = "longitude")
    int longitude;



}
