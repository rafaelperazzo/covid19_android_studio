package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

import com.github.mikephil.charting.data.Entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskGetEvolucaoMedia extends AsyncTask<Void,Void, ArrayList<Entry>> {


    AppDatabase db;
    TaskGetEvolucaoMediaResponse delegate;
    int periodo;
    String cidade;
    int tipo;
    ArrayList<String> labels;
    /*
    Tipo: 1 - Confirmações
    Tipo: 2 - Óbitos
    Tipo: 3 - Situação confirmações
    Tipo: 4 - Situação óbitos

     */

    public TaskGetEvolucaoMedia(AppDatabase db, TaskGetEvolucaoMediaResponse delegate, int periodo, String cidade, int tipo) {
        this.db = db;
        this.delegate = delegate;
        this.periodo = periodo;
        this.cidade = cidade;
        this.tipo = tipo;
        labels = new ArrayList<>();
    }

    @Override
    protected ArrayList<Entry> doInBackground(Void... voids) {
        List<EvolucaoMediaItem> items = db.evolucaoMediaDao().getAll(this.cidade,this.periodo);
        ArrayList<Entry> eixoY = new ArrayList<>();
        for (int i = 0; i< items.size(); i++) {
            if (this.tipo==1) {
                eixoY.add(new Entry(i,items.get(i).getConfirmados()));
            }
            else if (this.tipo==2) {
                eixoY.add(new Entry(i,items.get(i).getObitos()));
            }
            else if (this.tipo==3) {
                eixoY.add(new Entry(i,items.get(i).getSituacao_confirmados()));
            }
            else {
                eixoY.add(new Entry(i,items.get(i).getSituacao_obitos()));
            }
            labels.add(items.get(i).getData());
        }
        return eixoY;
    }

    @Override
    protected void onPostExecute(ArrayList<Entry> evolucaoMediaItems) {
        super.onPostExecute(evolucaoMediaItems);
        delegate.getEvolucaoFinish(evolucaoMediaItems,labels);
    }
}
