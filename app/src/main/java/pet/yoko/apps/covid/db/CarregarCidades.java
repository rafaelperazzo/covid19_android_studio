package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pet.yoko.apps.covid.CidadeAdapter;
import pet.yoko.apps.covid.CidadeItem;

public class CarregarCidades extends AsyncTask<Void, Void, List<CidadeItem>> {

    private AppDatabase db;
    private RecyclerView recyclerView;

    public CarregarCidades(AppDatabase db, RecyclerView recyclerView) {
        this.db = db;
        this.recyclerView = recyclerView;
    }

    @Override
    protected List<CidadeItem> doInBackground(Void... voids) {
        List<CidadeItem> items = db.cidadesNumerosDao().getAll();
        return items;
    }

    @Override
    protected void onPostExecute(List<CidadeItem> items) {
        super.onPostExecute(items);
        CidadeAdapter adapter = new CidadeAdapter(items);
        recyclerView.setAdapter(adapter);
    }
}
