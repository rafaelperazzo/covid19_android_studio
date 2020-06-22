package pet.yoko.apps.covid.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pet.yoko.apps.covid.CidadeItem;

@Dao
public interface DadosIniciaisDao {

    @Query("SELECT * FROM DadosIniciais")
    List<DadosIniciais> getAll();

    @Query("DELETE FROM DadosIniciais")
    void delete_all();

    @Insert
    void insert(DadosIniciais dadosIniciais);

    @Delete
    void delete(DadosIniciais dadosIniciais);

    @Update
    void update(DadosIniciais dadosIniciais);

}
