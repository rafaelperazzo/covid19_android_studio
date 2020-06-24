package pet.yoko.apps.covid.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BairroMapaItemDao {

    @Query("SELECT * FROM BairroMapaItem")
    List<BairroMapaItem> getAll();

    @Query("DELETE FROM BairroMapaItem")
    void delete_all();

    @Insert
    void insert(BairroMapaItem bairroMapaItem);

}
