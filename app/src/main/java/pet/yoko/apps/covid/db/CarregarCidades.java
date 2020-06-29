package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pet.yoko.apps.covid.CidadeAdapter;
import pet.yoko.apps.covid.CidadeItem;

public class CarregarCidades extends AsyncTask<Void, Void, List<CidadeItem>> {

    private AppDatabase db;
    private RecyclerView recyclerView;
    private CidadeAdapter adapter;
    List<CidadeItem> items;

    public CarregarCidades(AppDatabase db, RecyclerView recyclerView, CidadeAdapter cidadeAdapter, List<CidadeItem> items) {
        this.db = db;
        this.recyclerView = recyclerView;
        this.adapter=cidadeAdapter;
        this.items = items;
    }

    @Override
    protected List<CidadeItem> doInBackground(Void... voids) {
        List<CidadeItem> items = db.cidadesNumerosDao().getAll();
        this.items = items;
        return items;
    }

    @Override
    protected void onPostExecute(List<CidadeItem> items) {
        super.onPostExecute(items);
        this.items = items;
        this.adapter.setItems(items);
        this.adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
