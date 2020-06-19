package pet.yoko.apps.covid.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pet.yoko.apps.covid.CidadeItem;

@Database(entities = {CidadeItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CidadesNumerosDao cidadesNumerosDao();

}
