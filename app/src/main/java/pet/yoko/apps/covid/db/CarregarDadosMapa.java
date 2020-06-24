package pet.yoko.apps.covid.db;

import android.os.AsyncTask;
import java.util.List;

public class CarregarDadosMapa extends AsyncTask<Void, Void, List<CidadeMapaItem>> {

    AppDatabase db;

    public CarregarDadosMapa(AppDatabase db) {
        this.db = db;
    }

    @Override
    protected List<CidadeMapaItem> doInBackground(Void... voids) {
        List<CidadeMapaItem> cidades = db.cidadeMapaItemDao().getAll();
        return cidades;
    }
}
