package pet.yoko.apps.covid.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pet.yoko.apps.covid.CidadeItem;

@Database(entities = {CidadeItem.class,DadosIniciais.class,GraficoItem.class, CidadeMapaItem.class,BairroMapaItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CidadesNumerosDao cidadesNumerosDao();
    public abstract DadosIniciaisDao dadosIniciaisDao();
    public abstract GraficoItemDao graficoItemDao();
    public abstract CidadeMapaItemDao cidadeMapaItemDao();
    public abstract BairroMapaItemDao bairroMapaItemDao();

}
