package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

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

    private double calcularMediaCoeficientes(List<EvolucaoTotalItem> items) {
        EvolucaoTotalItem primeiro;
        EvolucaoTotalItem ultimo;
        double media = 0;
        int i = 0;
        double indice1 = 0;
        double indice2 = 0;
        double coeficiente;
        int proximo = 0;
        int periodos = 0;
        while (i<items.size()) {
            try {
                proximo = i+6;
                primeiro = items.get(i);
                ultimo = items.get(proximo);
                while (ultimo.getObitos()==primeiro.getObitos()) {
                    proximo = proximo + 1;
                    if (proximo<items.size()) {
                        ultimo = items.get(proximo);
                    }
                    else {
                        break;
                    }
                }

                if (this.cidade.equals("TODAS AS CIDADES")) {
                    indice1 = (primeiro.getObitos()*Ferramenta.cem)/(double)Ferramenta.populacao;
                    indice2 = (ultimo.getObitos()*Ferramenta.cem)/(double)Ferramenta.populacao;
                }
                else {
                    int populacao = db.cidadesNumerosDao().getPopulacao(this.cidade);
                    indice1 = (primeiro.getObitos()*Ferramenta.cem)/(double)populacao;
                    indice2 = (ultimo.getObitos()*Ferramenta.cem)/(double)populacao;

                }
            }
            catch (IndexOutOfBoundsException e) {
                break;
            }
            if (indice1-indice2==0) {
                coeficiente = 0; //TODO: DESCOBRIR!!
            }
            else {
                coeficiente = (proximo-i+1)/(indice1-indice2);
            }
            if (coeficiente!=0) {
                media = media + coeficiente;
                periodos = periodos+1;
            }
            i = proximo;
        }
        return(media/periodos);
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
                    indice1 = (primeiro.getObitos()*Ferramenta.cem)/(double)Ferramenta.populacao;
                    indice2 = (ultimo.getObitos()*Ferramenta.cem)/(double)Ferramenta.populacao;
                }
                else {
                    indice1 = (primeiro.getConfirmados()*Ferramenta.cem)/(double)Ferramenta.populacao;
                    indice2 = (ultimo.getConfirmados()*Ferramenta.cem)/(double)Ferramenta.populacao;
                }

            }
            else {
                int populacao = db.cidadesNumerosDao().getPopulacao(this.cidade);
                if (tipo==0) {
                    indice1 = (primeiro.getObitos()*Ferramenta.cem)/(double)populacao;
                    indice2 = (ultimo.getObitos()*Ferramenta.cem)/(double)populacao;
                }
                else {
                    indice1 = (primeiro.getConfirmados()*Ferramenta.cem)/(double)populacao;
                    indice2 = (ultimo.getConfirmados()*Ferramenta.cem)/(double)populacao;
                }

            }
            coeficiente = Ferramenta.TEMPO/(indice1-indice2);
            //coeficiente = calcularMediaCoeficientes(items);
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
