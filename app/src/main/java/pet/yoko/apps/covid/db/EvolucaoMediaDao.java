package pet.yoko.apps.covid.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EvolucaoMediaDao {

    @Query("SELECT * FROM (select * from EvolucaoMediaItem WHERE (data < date('now') AND cidade=:cidade) ORDER BY data DESC LIMIT :periodo) ORDER BY data")
    List<EvolucaoMediaItem> getAll(String cidade, int periodo);

    @Query("DELETE FROM EvolucaoMediaItem")
    void delete_all();

    @Insert
    void insert(EvolucaoMediaItem evolucaoMediaItem);

}
