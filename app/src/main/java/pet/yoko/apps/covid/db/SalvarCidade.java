package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

import pet.yoko.apps.covid.CidadeItem;

public class SalvarCidade extends AsyncTask<Void, Void, Void> {

    private CidadeItem cidadeNumero;
    private AppDatabase db;

    public SalvarCidade(CidadeItem cidadeNumero,AppDatabase db) {
        super();
        this.cidadeNumero = cidadeNumero;
        this.db = db;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db.cidadesNumerosDao().insert(cidadeNumero);
        return null;
    }
}
