package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

import java.text.DecimalFormat;
import java.util.List;

import pet.yoko.apps.covid.Ferramenta;


public class CarregarCoeficiente extends AsyncTask<Void, Void, Double> {

    AppDatabase db;
    String cidade = "TODAS AS CIDADES";
    int tipo = 0; //0 - ÓBITOS; 1 - CONFIRMAÇÕES

    public CarregarCoeficiente(AppDatabase db,String cidade,int tipo) {
        this.db = db;
        this.cidade = cidade;
        this.tipo = tipo;
    }

    @Override
    protected Double doInBackground(Void... voids) {
        double coeficiente = 0;
        List<EvolucaoTotalItem> items;
        if (this.cidade.equals("TODAS AS CIDADES")) {
            items = db.evolucaoItemDao().getAll7();
        }
        else {
            items = db.evolucaoItemDao().getAgrupadosCidade7(this.cidade);
        }

        try {
            EvolucaoTotalItem primeiro = items.get(0);
            EvolucaoTotalItem ultimo = items.get(items.size()-1);
            double indice1;
            double indice2;
            if (this.cidade.equals("TODAS AS CIDADES")) {
                if (tipo==0) {
                    indice1 = (primeiro.getObitos()*100000)/(double)Ferramenta.populacao;
                    indice2 = (ultimo.getObitos()*100000)/(double)Ferramenta.populacao;
                }
                else {
                    indice1 = (primeiro.getConfirmados()*100000)/(double)Ferramenta.populacao;
                    indice2 = (ultimo.getConfirmados()*100000)/(double)Ferramenta.populacao;
                }

            }
            else {
                int populacao = db.cidadesNumerosDao().getPopulacao(this.cidade);
                if (tipo==0) {
                    indice1 = (primeiro.getObitos()*100000)/(double)populacao;
                    indice2 = (ultimo.getObitos()*100000)/(double)populacao;
                }
                else {
                    indice1 = (primeiro.getConfirmados()*100000)/(double)populacao;
                    indice2 = (ultimo.getConfirmados()*100000)/(double)populacao;
                }

            }
            coeficiente = Ferramenta.TEMPO/(indice1-indice2);
        }
        catch (IndexOutOfBoundsException e) {
            coeficiente = 0;
        }
        catch (ArithmeticException e) {
            coeficiente = 0;
        }

        return (coeficiente);

    }

}
