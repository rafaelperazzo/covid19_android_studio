package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

public class DeletarCidades extends AsyncTask<Void, Void, Void> {

    AppDatabase db;

    public DeletarCidades(AppDatabase db) {
        this.db = db;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db.cidadesNumerosDao().delete_all();
        return null;
    }
}
