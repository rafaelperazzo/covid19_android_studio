package pet.yoko.apps.covid.db;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadData extends AsyncTask<Void, Void, Void> {

    private AppDatabase db;

    private OkHttpClient client = new OkHttpClient();

    public DownloadData(AppDatabase db) {
        this.db = db;
    }

    private String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private void processarJson(String url, String tipo) {
        String myResponse;
        try {
            myResponse = run(url);
            JSONArray arr = new JSONArray(myResponse);
            for (int i=0; i<arr.length();i++) {
                JSONObject obj = arr.getJSONObject(i);
                String label = obj.getString("label");
                int quantidade = obj.getInt("quantidade");
                GraficoItem item = new GraficoItem(tipo,label,quantidade);
                db.graficoItemDao().insert(item);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db.graficoItemDao().delete_all();
        processarJson("https://apps.yoko.pet/webapi/covidapi.php?dados=1&tipo=obitosPorSexo","obitosPorSexo");
        processarJson("https://apps.yoko.pet/webapi/covidapi.php?dados=1&tipo=obitosPorIdade","obitosPorIdade");
        processarJson("https://apps.yoko.pet/webapi/covidapi.php?dados=1&tipo=sexo","sexo");
        processarJson("https://apps.yoko.pet/webapi/covidapi.php?dados=1&tipo=idade","idade");
        processarJson("https://apps.yoko.pet/webapi/covidapi.php?dados=1&tipo=resumo","resumo");
        return null;
    }
}
