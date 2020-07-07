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

    @Query("SELECT populacao FROM CidadeItem WHERE cidade LIKE :cidade")
    int getPopulacao(String cidade);

    @Query("SELECT 0 as id,cidade,sum(confirmados) as confirmados, sum(suspeitos) as suspeitos, sum(obitos) as obitos, sum(populacao) as populacao, sum(recuperados) as recuperados, sum(confirmados)-sum(recuperados)-sum(obitos) as emRecuperacao, (sum(confirmados)-sum(recuperados)-sum(obitos))/(1.0*sum(populacao))*100000 as incidencia FROM CidadeItem")
    List<CidadeItem> getResumo();

    @Query("SELECT * FROM CidadeItem WHERE cidade LIKE :cidade")
    List<CidadeItem> getCidade(String cidade);



    @Query("DELETE FROM CidadeItem")
    void delete_all();

    @Insert
    void insert(CidadeItem cidadeNumero);

    @Delete
    void delete(CidadeItem cidadeNumero);

    @Update
    void update(CidadeItem cidadeNumero);

}
