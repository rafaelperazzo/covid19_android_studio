package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static final String TIPO = "confirmados";
    public static final String TITULO = "Confirmações por ";
    public static final String TIPO_GRAFICO = "bar";
    public String url = "https://apps.yoko.pet/webapi/covidapi.php?resumo=";
    public String url_cidades = "https://apps.yoko.pet/webapi/covidapi.php?dados=1&tipo=cidades";
    TextView confirmados;
    TextView suspeitos;
    TextView obitos;
    TextView taxa;
    TextView atualizacao;
    TextView confirmacoes;
    ProgressBar progresso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        atualizacao = (TextView)findViewById(R.id.atualizacao);
        confirmados = (TextView)findViewById(R.id.confirmados);
        suspeitos = (TextView)findViewById(R.id.suspeitos);
        obitos = (TextView)findViewById(R.id.obitos);
        taxa = (TextView)findViewById(R.id.taxa);
        confirmacoes = (TextView)findViewById(R.id.total_confirmacoes);
        progresso = (ProgressBar)findViewById(R.id.progresso);
        try {
            run(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnMapaCidadesClick(View view) {
        //Intent intent = new Intent(this, MapaCidadesActivity.class);
        Intent intent = new Intent(this, MapaActivity.class);
        startActivity(intent);

    }

    public void btnMapaBairrosClick(View view) {
        /*Intent intent = new Intent(this, MapaBairrosActivity.class);
        startActivity(intent);*/
        Intent intent = new Intent(this, TabelaActivity.class);
        startActivity(intent);

    }

    public void btnporIdadeClick(View view) {
        Intent intent =  new Intent(this,ChartActivity.class);
        intent.putExtra(TIPO,"idade");
        intent.putExtra(TITULO,"Confirmações por idade");
        intent.putExtra(TIPO_GRAFICO,"bar");
        startActivity(intent);
    }

    public void btnporSexoClick(View view) {
        Intent intent =  new Intent(this,ChartActivity.class);
        intent.putExtra(TIPO,"sexo");
        intent.putExtra(TITULO,"Confirmações por sexo");
        intent.putExtra(TIPO_GRAFICO,"bar");
        startActivity(intent);
    }

    public void confirmadosClick(View view) {
        /*Intent intent = new Intent(this, WebViewActivity.class);
        String SITE = "https://apps.yoko.pet/covid?q=5";
        intent.putExtra(TIPO,SITE);
        startActivity(intent);*/
        /*
        Intent intent =  new Intent(this,ChartActivity.class);
        intent.putExtra(TIPO,"sexo");
        intent.putExtra(TIPO_GRAFICO,"line");
        startActivity(intent);*/
    }

    void run(String url) throws IOException {
        progresso.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progresso.setVisibility(View.GONE);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ajustarDadosIniciais(myResponse);
                    }
                });

            }
        });
    }

    public void ajustarDadosIniciais(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            confirmados.setText(obj.getString("confirmados"));
            suspeitos.setText(obj.getString("suspeitos"));
            obitos.setText(obj.getString("obitos"));
            double dblTaxa = obj.getDouble("taxa");
            String strTaxa = String.format("%.2f",dblTaxa);
            taxa.setText(strTaxa);
            String data = obj.getString("atualizacao");
            atualizacao.setText(data);
            confirmacoes.setText(obj.getString("confirmacoes"));
            progresso.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
