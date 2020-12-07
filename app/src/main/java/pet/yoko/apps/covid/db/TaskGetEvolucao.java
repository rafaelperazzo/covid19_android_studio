package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class TaskGetEvolucao extends AsyncTask<Void,Void, List<EvolucaoTotalItem>> {

    AppDatabase db;
    TaskGetEvolucaoResponse delegate;

    public TaskGetEvolucao(AppDatabase db, TaskGetEvolucaoResponse delegate) {
        this.db = db;
        this.delegate = delegate;
    }

    @Override
    protected List<EvolucaoTotalItem> doInBackground(Void... voids) {
        List<EvolucaoTotalItem> items = db.evolucaoItemDao().getEvolucaoGeralObitos();
        List<EvolucaoTotalItem> mediaMovel = new ArrayList<>();

        for (int i=0; i<items.size()-7;i++) {
            int media_obitos = 0;
            int media_confirmacoes = 0;

            for (int j=i;j<i+7;j++) {
                media_obitos = media_obitos + (items.get(j).getObitos()-items.get(j+1).getObitos());
                media_confirmacoes = media_confirmacoes + (items.get(j).getConfirmados()-items.get(j+1).getConfirmados());
            }
            media_obitos = Math.round(media_obitos/(float)7);
            media_confirmacoes = Math.round(media_confirmacoes/(float)7);
            mediaMovel.add(new EvolucaoTotalItem("TODAS",items.get(i).getData(),media_confirmacoes,media_obitos));
        }

        return mediaMovel;
    }

    @Override
    protected void onPostExecute(List<EvolucaoTotalItem> evolucaoTotalItems) {
        super.onPostExecute(evolucaoTotalItems);
        delegate.getEvolucaoFinish(evolucaoTotalItems);
    }
}
