package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

import pet.yoko.apps.covid.Ferramenta;

public class GetPopulacao extends AsyncTask<Void, Void, Integer> {

    AppDatabase db;
    String cidade;

    public GetPopulacao(AppDatabase db, String cidade) {
        this.db = db;
        this.cidade = cidade;
    }

    @Override
    protected Integer doInBackground(Void... voids) {

        if (cidade.equals("TODAS AS CIDADES")) {
            return (Ferramenta.populacao);
        }
        else {
            int populacao = db.cidadesNumerosDao().getPopulacao(cidade);
            return (populacao);
        }

    }

}
