package pet.yoko.apps.covid.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GraficoItemDao {

    @Query(" select * from GraficoItem")
    List<GraficoItem> getAll();

    @Query(" select id,label,sum(quantidade) as quantidade,sum(quantidade2) as quantidade2 FROM GraficoItem WHERE tipo=:tipo GROUP by label")
    List<GraficoItem> getAgrupados(String tipo);

    @Query(" select id,label,sum(quantidade) as quantidade,sum(quantidade2) as quantidade2 FROM GraficoItem WHERE cidade LIKE :cidade AND tipo=:tipo GROUP by label")
    List<GraficoItem> getAgrupadosCidade(String cidade, String tipo);

    @Query("SELECT id,label,quantidade,quantidade2 FROM GraficoItem WHERE tipo LIKE :tipo")
    List<GraficoItem> getPorTipo(String tipo);

    @Query("DELETE FROM GraficoItem")
    void delete_all();

    @Insert
    void insert(GraficoItem graficoItem);

}
