package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

public class DeletarDadosIniciais extends AsyncTask<Void, Void, Void> {

    AppDatabase db;

    public DeletarDadosIniciais(AppDatabase db) {
        this.db = db;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db.dadosIniciaisDao().delete_all();
        return null;
    }
}
