package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

import java.util.List;

public class CarregarDadosMapaBairros extends AsyncTask<Void, Void, List<BairroMapaItem>> {

    AppDatabase db;

    public CarregarDadosMapaBairros(AppDatabase db) {
        this.db = db;
    }

    @Override
    protected List<BairroMapaItem> doInBackground(Void... voids) {
        List<BairroMapaItem> bairros = db.bairroMapaItemDao().getAll();
        return bairros;
    }
}
