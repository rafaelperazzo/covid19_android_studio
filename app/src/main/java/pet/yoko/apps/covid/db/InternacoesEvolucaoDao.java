package pet.yoko.apps.covid.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface InternacoesEvolucaoDao {

    @Query("SELECT * FROM (select * from InternacoesEvolucaoItem WHERE (data < date('now')) ORDER BY data DESC LIMIT :periodo) ORDER BY data")
    List<InternacoesEvolucaoItem> getAll(int periodo);

    @Query("DELETE FROM InternacoesEvolucaoItem")
    void delete_all();

    @Insert
    void insert(InternacoesEvolucaoItem internacoesEvolucaoItem);

}
