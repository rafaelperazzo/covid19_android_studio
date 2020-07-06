package pet.yoko.apps.covid.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EvolucaoItemDao {

    @Query("select id,cidade,data,sum(confirmados) as confirmados,sum(obitos) as obitos FROM EvolucaoTotalItem GROUP by data ORDER BY data")
    List<EvolucaoTotalItem> getAll();

    @Query("select id,cidade,data,sum(confirmados) as confirmados,sum(obitos) as obitos FROM EvolucaoTotalItem GROUP by data ORDER BY data DESC LIMIT 7")
    List<EvolucaoTotalItem> getAll7();

    @Query("select id,cidade,data,sum(confirmados) as confirmados,sum(obitos) as obitos FROM EvolucaoTotalItem WHERE cidade=:cidade GROUP by data ORDER BY data")
    List<EvolucaoTotalItem> getAgrupadosCidade(String cidade);

    @Query("select id,cidade,data,sum(confirmados) as confirmados,sum(obitos) as obitos FROM EvolucaoTotalItem WHERE cidade=:cidade GROUP by data ORDER BY data DESC LIMIT 7")
    List<EvolucaoTotalItem> getAgrupadosCidade7(String cidade);

    @Query("DELETE FROM EvolucaoTotalItem")
    void delete_all();

    @Insert
    void insert(EvolucaoTotalItem evolucaoTotalItem);

}
