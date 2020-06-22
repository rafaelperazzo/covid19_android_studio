package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

public class SalvarDadosIniciais extends AsyncTask<Void, Void, Void> {

    private AppDatabase db;
    private DadosIniciais dadosIniciais;

    public SalvarDadosIniciais(AppDatabase db, DadosIniciais dadosIniciais) {
        super();
        this.db = db;
        this.dadosIniciais = dadosIniciais;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db.dadosIniciaisDao().insert(dadosIniciais);
        return null;
    }
}
