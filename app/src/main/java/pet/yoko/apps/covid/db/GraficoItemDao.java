package pet.yoko.apps.covid.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GraficoItemDao {

    @Query("SELECT * FROM GraficoItem")
    List<GraficoItem> getAll();

    @Query("SELECT id,label,quantidade FROM GraficoItem WHERE tipo LIKE :tipo")
    List<GraficoItem> getPorTipo(String tipo);

    @Query("DELETE FROM GraficoItem")
    void delete_all();

    @Insert
    void insert(GraficoItem graficoItem);

}
