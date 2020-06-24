package pet.yoko.apps.covid.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CidadeMapaItemDao {

    @Query("SELECT * FROM CidadeMapaItem")
    List<CidadeMapaItem> getAll();

    @Query("DELETE FROM CidadeMapaItem")
    void delete_all();

    @Insert
    void insert(CidadeMapaItem cidadeMapaItem);

}
