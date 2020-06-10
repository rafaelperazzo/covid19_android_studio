package pet.yoko.apps.covid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    public static final String TIPO = "confirmados";
    public static final String TITULO = "Confirmações por ";
    public static final String TIPO_GRAFICO = "bar";
    public static final String TIPO_MAPA = "cidades";
    public String url = "https://apps.yoko.pet/webapi/covidapi.php?resumo=";
    public String url_cidades = "https://apps.yoko.pet/webapi/covidapi.php?dados=1&tipo=cidades";
    TextView confirmados;
    TextView suspeitos;
    TextView obitos;
    TextView taxa;
    TextView atualizacao;
    TextView confirmacoes;
    TextView txtUti;
    TextView txtEnfermaria;
    ProgressBar progresso;
    TextView versao;
    public int VERSAO;
    TextView atualizar;

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
        txtUti = (TextView)findViewById(R.id.txtUTI);
        txtEnfermaria = (TextView)findViewById(R.id.txtEnfermaria);
        progresso = (ProgressBar)findViewById(R.id.progresso);
        versao = (TextView)findViewById(R.id.txtVersao);
        atualizar = (TextView)findViewById(R.id.txtAtualizar);
        atualizar.setVisibility(View.GONE);
        String versionName;
        int versionCode;
        versao.setText("Versão: " + String.valueOf(getVersionCode()));
        VERSAO = getVersionCode();
        try {
            run(url,0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //VERIFICANDO POR ATUALIZAÇÃO
        try {
            run("https://play.google.com/store/apps/details?id=pet.yoko.apps.covid",1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.run("https://apps.yoko.pet/webapi/covidapi.php?dados=1&tipo=dadosInternacoes",2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void atualizarClick(View v) {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=pet.yoko.apps.covid");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public int getVersionCode() {
        int versionCode = 0;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (versionCode);
    }

    public void showMapasMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.mapas);
        popup.show();
    }

    public void showIdadeMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menuidade);
        popup.show();
    }

    public void showSexoMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menusexo);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.mapas_cidades:
                intent = new Intent(this, MapaActivity.class);
                intent.putExtra(TIPO_MAPA,"cidades");
                startActivity(intent);
                return true;
            case R.id.mapas_bairros:
                intent = new Intent(this, MapaActivity.class);
                intent.putExtra(TIPO_MAPA,"bairros");
                startActivity(intent);
                return true;
            case R.id.idade_confirmados:
                intent = new Intent(this, ChartActivity.class);
                intent.putExtra(TIPO,"idade");
                intent.putExtra(TIPO_GRAFICO,"bar");
                intent.putExtra(TITULO,"Casos confirmados por idade (totais)");
                startActivity(intent);
                return true;
            case R.id.idade_obitos:
                intent = new Intent(this, ChartActivity.class);
                intent.putExtra(TIPO,"obitosPorIdade");
                intent.putExtra(TIPO_GRAFICO,"bar");
                intent.putExtra(TITULO,"Óbitos confirmados por idade (totais)");
                startActivity(intent);
                return true;
            case R.id.sexo_confirmados:
                intent = new Intent(this, ChartActivity.class);
                intent.putExtra(TIPO,"sexo");
                intent.putExtra(TIPO_GRAFICO,"bar");
                intent.putExtra(TITULO,"Casos confirmados por sexo (totais)");
                startActivity(intent);
                return true;
            case R.id.sexo_obitos:
                intent = new Intent(this, ChartActivity.class);
                intent.putExtra(TIPO,"obitosPorSexo");
                intent.putExtra(TIPO_GRAFICO,"bar");
                intent.putExtra(TITULO,"Óbitos confirmados por sexo (totais)");
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {

            case R.id.menu_evolucao_temporal:
                intent =  new Intent(this,ChartActivity.class);
                intent.putExtra(TIPO,"evolucao");
                intent.putExtra(TITULO,"Evolução ao longo do tempo");
                intent.putExtra(TIPO_GRAFICO,"line");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnMapaBairrosClick(View view) {
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
        Intent intent =  new Intent(this,ChartActivity.class);
        intent.putExtra(TIPO,"evolucao");
        intent.putExtra(TITULO,"Evolução ao longo do tempo");
        intent.putExtra(TIPO_GRAFICO,"line");
        startActivity(intent);
    }

    void run(String url, final int tipo) throws IOException {
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
                        if (tipo==0) {
                            ajustarDadosIniciais(myResponse);
                        }
                        else if (tipo==1){
                            verificarAtualizacao(myResponse);
                        }
                        else {
                            ajustarTaxaOcupacaoLeitos(myResponse);
                        }

                    }
                });

            }
        });
    }

    public void verificarAtualizacao(String myResponse) {
        int versaoNova = Ferramenta.getAppPlayStoreVersion(myResponse);
        if (VERSAO<versaoNova) {
            versao.setText("UMA NOVA VERSÃO ESTÁ DISPONÍVEL!");
            versao.setTextColor(Color.RED);
            atualizar.setVisibility(View.VISIBLE);
        }
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
            progresso.setVisibility(View.GONE);
        }
    }

    public void ajustarTaxaOcupacaoLeitos(String myResponse) {
        try {
            JSONArray arr = new JSONArray(myResponse);
            for (int i=0; i<arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                int uti = obj.getInt("uti");
                int enfermaria = obj.getInt("enfermaria");
                this.txtUti.setText(String.valueOf(uti) + "%");
                this.txtEnfermaria.setText(String.valueOf(enfermaria) + "%");
                if (uti>70) {
                    this.txtUti.setBackgroundColor(Color.parseColor("#660000"));
                    this.txtUti.setTextColor(Color.WHITE);
                }
                else {
                    this.txtUti.setBackgroundColor(Color.parseColor("#009900"));
                    this.txtUti.setTextColor(Color.WHITE);
                }
                if (enfermaria>70) {
                    this.txtEnfermaria.setBackgroundColor(Color.parseColor("#660000"));
                    this.txtEnfermaria.setTextColor(Color.WHITE);
                }
                else {
                    this.txtEnfermaria.setBackgroundColor(Color.parseColor("#009900"));
                    this.txtEnfermaria.setTextColor(Color.WHITE);
                }
                progresso.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            progresso.setVisibility(View.GONE);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            progresso.setVisibility(View.GONE);
        }
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

}
