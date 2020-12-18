package pet.yoko.apps.covid.db;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.skydoves.progressview.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pet.yoko.apps.covid.CidadeItem;
import pet.yoko.apps.covid.Ferramenta;

public class DownloadData extends AsyncTask<Void, Void, Void> {

    private AppDatabase db;
    ProgressView progresso;
    DownloadDataResponse delegate;

    private OkHttpClient client = new OkHttpClient();

    public DownloadData(AppDatabase db, ProgressView progresso, DownloadDataResponse delegate) {
        this.db = db;
        this.progresso = progresso;
        this.delegate = delegate;
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
                GraficoItem item;
                if (tipo.equals("coeficiente") || tipo.equals("evolucao")) {
                    int quantidade2 = obj.getInt("quantidade2");
                    item = new GraficoItem(tipo,label,quantidade,quantidade2);
                }
                else {
                    item = new GraficoItem(tipo,label,quantidade,0);
                }

                db.graficoItemDao().insert(item);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private String coeficiente2situacao(double coeficiente) {
        if ((coeficiente>=0) && (coeficiente<0.58)) {
            return ("MUITO ALTO");
        }
        if ((coeficiente>=0.58) && (coeficiente<5)) {
            return ("ALTO");
        }
        if ((coeficiente>=5) && (coeficiente<20)) {
            return ("MODERADO");
        }
        if ((coeficiente>=20) && (coeficiente<56.8)) {
            return ("BAIXO");
        }
        else {
            return ("NORMALIDADE");
        }
    }

    private void baixarDadosCidades(String url) {
        try {
            String myResponse = run(url);
            JSONArray arr = new JSONArray(myResponse);
            db.cidadesNumerosDao().delete_all();
            for (int i=0; i<arr.length();i++) {
                JSONObject linha = arr.getJSONObject(i);
                String cidade = linha.getString("cidade");
                int confirmados = linha.getInt("confirmados");
                int suspeitos = linha.getInt("suspeitos");
                int obitos = linha.getInt("obitos");
                double incidencia = linha.getDouble("taxa");
                int recuperados = linha.getInt("recuperados");
                int emRecuperacao = confirmados-recuperados;
                int populacao = linha.getInt("populacao");
                int primeiro = linha.getInt("primeiro");
                double indice1 = ((obitos)*100000)/((double)populacao);
                double indice2 = ((primeiro)*100000)/((double)populacao);
                double coeficiente = 1000;
                if (indice1-indice2!=0) {
                    coeficiente = Ferramenta.TEMPO/((indice1-indice2));
                }

                String situacao = coeficiente2situacao(coeficiente);
                CidadeItem cidadeNumero = new CidadeItem(cidade,confirmados,suspeitos,obitos,incidencia,recuperados,emRecuperacao,populacao,situacao);
                db.cidadesNumerosDao().insert(cidadeNumero);
            }
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void baixarDadosIniciais(String url) {
        try {
            db.dadosIniciaisDao().delete_all();
            String myResponse = run(url);
            JSONObject obj = new JSONObject(myResponse);
            int emRecuperacao = obj.getInt("confirmados") - obj.getInt("recuperados");
            DadosIniciais di = new DadosIniciais(obj.getInt("confirmados"),obj.getInt("obitos"),obj.getInt("recuperados"),emRecuperacao,obj.getString("atualizacao"),obj.getDouble("taxa"),obj.getInt("confirmacoes"),obj.getString("comorbidades"),obj.getDouble("porDia"),obj.getDouble("mediana_idade"),obj.getInt("uti"),obj.getInt("enfermaria"));
            db.dadosIniciaisDao().insert(di);
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void baixarDadosCidadesMapa(String url) {
        try {
            String myResponse = run(url);
            JSONArray arr = new JSONArray(myResponse);
            db.cidadeMapaItemDao().delete_all();
            for (int i=0; i<arr.length();i++) {
                JSONObject obj = arr.getJSONObject(i);
                CidadeMapaItem cidade = new CidadeMapaItem(obj.getString("cidade"),obj.getInt("confirmados"),obj.getInt("emRecuperacao"),obj.getInt("obitos"),obj.getInt("recuperados"),obj.getDouble("latitude"),obj.getDouble("longitude"),obj.getDouble("incidencia"));
                db.cidadeMapaItemDao().insert(cidade);
            }
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void baixarDadosBairrosMapa(String url) {
        try {
            String myResponse = run(url);
            JSONArray arr = new JSONArray(myResponse);
            db.bairroMapaItemDao().delete_all();
            for (int i=0; i<arr.length();i++) {
                JSONObject obj = arr.getJSONObject(i);
                BairroMapaItem bairro = new BairroMapaItem(obj.getString("cidade"),obj.getString("bairro"),obj.getInt("confirmados"),obj.getDouble("latitude"),obj.getDouble("longitude"));
                db.bairroMapaItemDao().insert(bairro);
            }
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void baixarDadosEvolucaoTotal(String url) {
        try {
            String myResponse = run(url);
            JSONArray arr = new JSONArray(myResponse);
            db.evolucaoItemDao().delete_all();
            for (int i=0; i<arr.length();i++) {
                JSONObject obj = arr.getJSONObject(i);
                EvolucaoTotalItem evolucao = new EvolucaoTotalItem(obj.getString("cidade"),obj.getString("data"),obj.getInt("confirmado"),obj.getInt("obitos"));
                db.evolucaoItemDao().insert(evolucao);
            }
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void baixarDadosEvolucaoMedia(String url) {
        try {
            String myResponse = run(url);
            JSONArray arr = new JSONArray(myResponse);
            db.evolucaoMediaDao().delete_all();
            for (int i=0; i<arr.length();i++) {
                JSONObject obj = arr.getJSONObject(i);
                EvolucaoMediaItem evolucaoMedia = new EvolucaoMediaItem(obj.getString("cidade"),obj.getString("data"),(float)obj.getDouble("media_confirmacoes"),(float)obj.getDouble("media"),obj.getInt("situacao_confirmacoes"),obj.getInt("situacao"));
                db.evolucaoMediaDao().insert(evolucaoMedia);
            }
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progresso.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progresso.setVisibility(View.GONE);
        this.delegate.downloadDataFinish();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db.graficoItemDao().delete_all();
        processarJson("https://sci02-ter-jne.ufca.edu.br/webapi/covidapi.php?dados=1&tipo=obitosPorSexo","obitosPorSexo");
        progresso.setProgress(5);
        progresso.setLabelText("5%");
        processarJson("https://sci02-ter-jne.ufca.edu.br/webapi/covidapi.php?dados=1&tipo=obitosPorIdade","obitosPorIdade");
        progresso.setProgress(10);
        progresso.setLabelText("10%");
        processarJson("https://sci02-ter-jne.ufca.edu.br/webapi/covidapi.php?dados=1&tipo=sexo","sexo");
        progresso.setProgress(15);
        progresso.setLabelText("15%");
        processarJson("https://sci02-ter-jne.ufca.edu.br/webapi/covidapi.php?dados=1&tipo=idade","idade");
        progresso.setProgress(20);
        progresso.setLabelText("20%");
        processarJson("https://sci02-ter-jne.ufca.edu.br/webapi/covidapi.php?dados=1&tipo=resumo","resumo");
        progresso.setProgress(25);
        progresso.setLabelText("25%");
        baixarDadosCidades("https://sci02-ter-jne.ufca.edu.br/webapi/covidapi.php?dados=1&tipo=hoje");
        progresso.setProgress(30);
        progresso.setLabelText("30%");
        baixarDadosIniciais("https://sci02-ter-jne.ufca.edu.br/webapi/covidapi.php?dados=1&tipo=dadosIniciais");
        progresso.setProgress(40);
        progresso.setLabelText("40%");
        baixarDadosCidadesMapa("https://sci02-ter-jne.ufca.edu.br/webapi/covidapi.php?dados=1&tipo=cidades");
        progresso.setProgress(45);
        progresso.setLabelText("45%");
        baixarDadosBairrosMapa("https://sci02-ter-jne.ufca.edu.br/webapi/covidapi.php?dados=1&tipo=bairros");
        progresso.setProgress(60);
        progresso.setLabelText("60%");
        baixarDadosEvolucaoTotal("https://sci02-ter-jne.ufca.edu.br/webapi/covidapi.php?dados=1&tipo=evolucaoTotal");
        progresso.setProgress(80);
        progresso.setLabelText("80%");
        baixarDadosEvolucaoMedia("https://sci02-ter-jne.ufca.edu.br/webapi/covidapi.php?dados=1&tipo=media");
        progresso.setProgress(100);
        progresso.setLabelText("100%");
        return null;
    }



}
