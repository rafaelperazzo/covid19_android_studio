package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

import java.text.DecimalFormat;
import java.util.List;


public class CarregarCoeficiente extends AsyncTask<Void, Void, String> {

    AppDatabase db;

    public CarregarCoeficiente(AppDatabase db) {
        this.db = db;
    }

    @Override
    protected String doInBackground(Void... voids) {
        List<GraficoItem> graficoItems = db.graficoItemDao().getPorTipo("coeficiente");
        int ultimo_indice = graficoItems.size()-1;
        GraficoItem primeiro = graficoItems.get(0);
        GraficoItem ultimo = graficoItems.get(ultimo_indice);
        double coeficiente = (ultimo.getQuantidade()-primeiro.getQuantidade())/(double)15;
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(coeficiente);
    }

}
