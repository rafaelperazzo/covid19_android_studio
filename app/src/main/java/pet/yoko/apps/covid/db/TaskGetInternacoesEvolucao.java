package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class TaskGetInternacoesEvolucao extends AsyncTask <Void,Void, ArrayList<Entry>> {

    AppDatabase db;
    TaskGetInternacoesEvolucaoResponse delegate;
    int periodo;
    ArrayList<String> labels;

    public TaskGetInternacoesEvolucao(AppDatabase db, TaskGetInternacoesEvolucaoResponse delegate, int periodo) {
        this.db = db;
        this.delegate = delegate;
        this.periodo = periodo;
        this.labels = new ArrayList<>();
    }



    @Override
    protected ArrayList<Entry> doInBackground(Void... voids) {
        List<InternacoesEvolucaoItem> items = db.internacoesEvolucaoDao().getAll(this.periodo);
        ArrayList<Entry> eixoY = new ArrayList<>();
        for (int i = 0; i< items.size(); i++) {
            eixoY.add(new Entry(i,items.get(i).getOcupacao()));
            labels.add(items.get(i).getData());
        }
        return eixoY;
    }

    @Override
    protected void onPostExecute(ArrayList<Entry> entries) {
        super.onPostExecute(entries);
        delegate.getInternacoesFinish(entries,labels);
    }
}
