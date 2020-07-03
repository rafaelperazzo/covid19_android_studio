package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

import java.text.DecimalFormat;
import java.util.List;

import pet.yoko.apps.covid.Ferramenta;


public class CarregarCoeficiente extends AsyncTask<Void, Void, Double> {

    AppDatabase db;

    public CarregarCoeficiente(AppDatabase db) {
        this.db = db;
    }

    @Override
    protected Double doInBackground(Void... voids) {
        List<GraficoItem> graficoItems = db.graficoItemDao().getPorTipo("coeficiente");
        int ultimo_indice = graficoItems.size()-1;
        double coeficiente;

        try {
            GraficoItem primeiro = graficoItems.get(0);
            GraficoItem ultimo = graficoItems.get(ultimo_indice);
            double indice1 = ((primeiro.getQuantidade2()*100000)/(double) Ferramenta.populacao);
            double indice2 = ((ultimo.getQuantidade2()*100000)/(double) Ferramenta.populacao);
            coeficiente = Ferramenta.TEMPO/(indice1-indice2);
        }
        catch (IndexOutOfBoundsException e) {
            coeficiente = 0;
        }
        catch (ArithmeticException e) {
            coeficiente = 0;
        }

        //DecimalFormat df = new DecimalFormat("#0.00");
        //return df.format(coeficiente);
        return(coeficiente);
    }

}
