package pet.yoko.apps.covid.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EvolucaoMediaDao {

    @Query("select * from EvolucaoMediaItem WHERE cidade=:cidade ORDER BY data ASC LIMIT :periodo")
    List<EvolucaoMediaItem> getAll(String cidade, int periodo);

    @Query("DELETE FROM EvolucaoMediaItem")
    void delete_all();

    @Insert
    void insert(EvolucaoMediaItem evolucaoMediaItem);

}
