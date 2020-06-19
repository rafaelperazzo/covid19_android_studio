package pet.yoko.apps.covid.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pet.yoko.apps.covid.CidadeItem;

@Dao
public interface CidadesNumerosDao {

    @Query("SELECT * FROM CidadeItem")
    List<CidadeItem> getAll();

    @Query("DELETE FROM CidadeItem")
    void delete_all();

    @Insert
    void insert(CidadeItem cidadeNumero);

    @Delete
    void delete(CidadeItem cidadeNumero);

    @Update
    void update(CidadeItem cidadeNumero);

}
