package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

import java.util.List;

public class CarregarEvolucaoTotal extends AsyncTask<Void,Void, List<EvolucaoTotalItem>> {

    private AppDatabase db;
    String tipo; //Geral e últimos 7 dias;
    String cidade;

    public CarregarEvolucaoTotal(AppDatabase db, String tipo, String cidade) {
        this.db = db;
        this.tipo = tipo;
        this.cidade = cidade;
    }

    @Override
    protected List<EvolucaoTotalItem> doInBackground(Void... voids) {
        List<EvolucaoTotalItem> items;
        if (cidade.equals("TODAS")) { //TODAS AS CIDADES
            if (tipo.equals("GERAL")) {
                items = db.evolucaoItemDao().getAll();
            }
            else {
                items = db.evolucaoItemDao().getAll7();
            }
        }
        else { //CIDADE EM PARTICULAR
            if (tipo.equals("GERAL")) {
                items = db.evolucaoItemDao().getAgrupadosCidade(this.cidade);
            }
            else {
                items = db.evolucaoItemDao().getAgrupadosCidade(this.cidade);
            }
        }
        return items;
    }
}
