package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    public String url = "https://apps.yoko.pet/api/covidapi.php?resumo=";
    TextView confirmados;
    TextView suspeitos;
    TextView obitos;
    TextView taxa;
    TextView atualizacao;
    TextView confirmacoes;

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

        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnMapaCidadesClick(View view) {
        Intent intent = new Intent(this, MapaCidadesActivity.class);
        startActivity(intent);

    }

    public void btnMapaBairrosClick(View view) {
        Intent intent = new Intent(this, MapaBairrosActivity.class);
        startActivity(intent);

    }

    public void btnporIdadeClick(View view) {
        Intent intent = new Intent(this, PorIdadeActivity.class);
        startActivity(intent);

    }

    public void btnporSexoClick(View view) {
        Intent intent = new Intent(this, PorSexoActivity.class);
        startActivity(intent);

    }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(myResponse);
                            confirmados.setText(obj.getString("confirmados"));
                            suspeitos.setText(obj.getString("suspeitos"));
                            obitos.setText(obj.getString("obitos"));
                            double dblTaxa = obj.getDouble("taxa");
                            String strTaxa = String.format("%.2f",dblTaxa);
                            taxa.setText(strTaxa);
                            String data = obj.getString("atualizacao");
                            atualizacao.setText(data);
                            confirmacoes.setText(obj.getString("confirmacoes"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

}
